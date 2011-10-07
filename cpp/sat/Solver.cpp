/****************************************************************************************[Solver.C]
MiniSat -- Copyright (c) 2003-2005, Niklas Een, Niklas Sorensson

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or
substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT
OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
**************************************************************************************************/

#include "Solver.h"
#include "Sort.h"
#include <cmath>


//=================================================================================================
// Helper functions:


bool removeWatch(vec<GClause>& ws, GClause elem)    // Pre-condition: 'elem' must exists in 'ws' OR 'ws' must be empty.
{
    if (ws.size() == 0) return false;     // (skip lists that are already cleared)
    int j = 0;
    for (; ws[j] != elem  ; j++) assert(j < ws.size());
    for (; j < ws.size()-1; j++) ws[j] = ws[j+1];
    ws.pop();
    return true;
}


//=================================================================================================
// Operations on clauses:


/*_________________________________________________________________________________________________
|
|  newClause : (ps : const vec<Lit>&) (learnt : bool)  ->  [void]
|  
|  Description:
|    Allocate and add a new clause to the SAT solvers clause database. If a conflict is detected,
|    the 'ok' flag is cleared and the solver is in an unusable state (must be disposed).
|  
|  Input:
|    ps     - The new clause as a vector of literals.
|    learnt - Is the clause a learnt clause? For learnt clauses, 'ps[0]' is assumed to be the
|             asserting literal. An appropriate 'enqueue()' operation will be performed on this
|             literal. One of the watches will always be on this literal, the other will be set to
|             the literal with the highest decision level.
|  
|  Effect:
|    Activity heuristics are updated.
|________________________________________________________________________________________________@*/
void Solver::newClause(const vec<Lit>& ps_, bool learnt)
{
    if (!ok) return;

    vec<Lit>    qs;
    if (!learnt){
        assert(decisionLevel() == 0);
        ps_.copyTo(qs);             // Make a copy of the input vector.

        // Remove duplicates:
        sortUnique(qs);

        // Check if clause is satisfied:
        for (int i = 0; i < qs.size()-1; i++){
            if (qs[i] == ~qs[i+1])
                return; }
        for (int i = 0; i < qs.size(); i++){
            if (value(qs[i]) == l_True)
                return; }

        // Remove false literals:
        int     i, j;
        for (i = j = 0; i < qs.size(); i++)
            if (value(qs[i]) != l_False)
                qs[j++] = qs[i];
        qs.shrink(i - j);
    }
    const vec<Lit>& ps = learnt ? ps_ : qs;     // 'ps' is now the (possibly) reduced vector of literals.

    if (ps.size() == 0){
        ok = false;

    }else if (ps.size() == 1){
        // NOTE: If enqueue takes place at root level, the assignment will be lost in incremental use (it doesn't seem to hurt much though).
        if (!enqueue(ps[0]))
            ok = false;

    }else if (ps.size() == 2){
        // Create special binary clause watch:
        watches[index(~ps[0])].push(GClause_new(ps[1]));
        watches[index(~ps[1])].push(GClause_new(ps[0]));

        if (learnt){
            check(enqueue(ps[0], GClause_new(~ps[1])));
            stats.learnts_literals += ps.size();
        }else
            stats.clauses_literals += ps.size();
        n_bin_clauses++;

    }else{
        // Allocate clause:
        Clause* c   = Clause_new(learnt, ps);

        if (learnt){
            // Put the second watch on the literal with highest decision level:
            int     max_i = 1;
            int     max   = level[var(ps[1])];
            for (int i = 2; i < ps.size(); i++)
                if (level[var(ps[i])] > max)
                    max   = level[var(ps[i])],
                    max_i = i;
            (*c)[1]     = ps[max_i];
            (*c)[max_i] = ps[1];

            // Bump, enqueue, store clause:
            claBumpActivity(c);         // (newly learnt clauses should be considered active)
            check(enqueue((*c)[0], GClause_new(c)));
            learnts.push(c);
            stats.learnts_literals += c->size();
        }else{
            // Store clause:
            clauses.push(c);
            stats.clauses_literals += c->size();
        }
        // Watch clause:
        watches[index(~(*c)[0])].push(GClause_new(c));
        watches[index(~(*c)[1])].push(GClause_new(c));
    }
}


// Disposes a clauses and removes it from watcher lists. NOTE! Low-level; does NOT change the 'clauses' and 'learnts' vector.
//
void Solver::remove(Clause* c, bool just_dealloc)
{
    if (!just_dealloc){
        if (c->size() == 2)
            removeWatch(watches[index(~(*c)[0])], GClause_new((*c)[1])),
            removeWatch(watches[index(~(*c)[1])], GClause_new((*c)[0]));
        else
            removeWatch(watches[index(~(*c)[0])], GClause_new(c)),
            removeWatch(watches[index(~(*c)[1])], GClause_new(c));
    }

    if (c->learnt()) stats.learnts_literals -= c->size();
    else             stats.clauses_literals -= c->size();

    xfree(c);
}


// Can assume everything has been propagated! (esp. the first two literals are != l_False, unless
// the clause is binary and satisfied, in which case the first literal is true)
// Returns True if clause is satisfied (will be removed), False otherwise.
//
bool Solver::simplify(Clause* c) const
{
    assert(decisionLevel() == 0);
    for (int i = 0; i < c->size(); i++){
        if (value((*c)[i]) == l_True)
            return true;
    }
    return false;
}


//=================================================================================================
// Minor methods:


// Creates a new SAT variable in the solver. If 'decision_var' is cleared, variable will not be
// used as a decision variable (NOTE! This has effects on the meaning of a SATISFIABLE result).
//
Var Solver::newVar() {
    int     index;
    index = nVars();
    watches     .push();          // (list for positive literal)
    watches     .push();          // (list for negative literal)
    reason      .push(GClause_NULL);
    assigns     .push(toInt(l_Undef));
    level       .push(-1);
    activity    .push(0);
    order       .newVar();
    analyze_seen.push(0);
    return index; }


// Returns FALSE if immediate conflict.
bool Solver::assume(Lit p) {
    trail_lim.push(trail.size());
    return enqueue(p); }


// Revert to the state at given level.
void Solver::cancelUntil(int level) {
    if (decisionLevel() > level){
        for (int c = trail.size()-1; c >= trail_lim[level]; c--){
            Var     x  = var(trail[c]);
            assigns[x] = toInt(l_Undef);
            reason [x] = GClause_NULL;
            order.undo(x); }
        trail.shrink(trail.size() - trail_lim[level]);
        trail_lim.shrink(trail_lim.size() - level);
        qhead = trail.size(); } }


//=================================================================================================
// Major methods:


/*_________________________________________________________________________________________________
|
|  analyze : (confl : Clause*) (out_learnt : vec<Lit>&) (out_btlevel : int&)  ->  [void]
|  
|  Description:
|    Analyze conflict and produce a reason clause.
|  
|    Pre-conditions:
|      * 'out_learnt' is assumed to be cleared.
|      * Current decision level must be greater than root level.
|  
|    Post-conditions:
|      * 'out_learnt[0]' is the asserting literal at level 'out_btlevel'.
|  
|  Effect:
|    Will undo part of the trail, upto but not beyond the assumption of the current decision level.
|________________________________________________________________________________________________@*/
void Solver::analyze(Clause* _confl, vec<Lit>& out_learnt, int& out_btlevel)
{
    GClause confl = GClause_new(_confl);
    vec<char>&     seen  = analyze_seen;
    int            pathC = 0;
    Lit            p     = lit_Undef;

    // Generate conflict clause:
    //
    out_learnt.push();      // (leave room for the asserting literal)
    out_btlevel = 0;
    int index = trail.size()-1;
    do{
        assert(confl != GClause_NULL);          // (otherwise should be UIP)

        Clause& c = confl.isLit() ? ((*analyze_tmpbin)[1] = confl.lit(), *analyze_tmpbin)
                                  : *confl.clause();
        if (c.learnt())
            claBumpActivity(&c);

        for (int j = (p == lit_Undef) ? 0 : 1; j < c.size(); j++){
            Lit q = c[j];
            if (!seen[var(q)] && level[var(q)] > 0){
                varBumpActivity(q);
                seen[var(q)] = 1;
                if (level[var(q)] == decisionLevel())
                    pathC++;
                else{
                    out_learnt.push(q);
                    out_btlevel = max(out_btlevel, level[var(q)]);
                }
            }
        }

        // Select next clause to look at:
        while (!seen[var(trail[index--])]);
        p     = trail[index+1];
        confl = reason[var(p)];
        seen[var(p)] = 0;
        pathC--;

    }while (pathC > 0);
    out_learnt[0] = ~p;

    int     i, j;
    if (expensive_ccmin){
        // Simplify conflict clause (a lot):
        //
        uint    min_level = 0;
        for (i = 1; i < out_learnt.size(); i++)
            min_level |= 1 << (level[var(out_learnt[i])] & 31);         // (maintain an abstraction of levels involved in conflict)

        out_learnt.copyTo(analyze_toclear);
        for (i = j = 1; i < out_learnt.size(); i++)
            if (reason[var(out_learnt[i])] == GClause_NULL || !analyze_removable(out_learnt[i], min_level))
                out_learnt[j++] = out_learnt[i];
    }else{
        // Simplify conflict clause (a little):
        //
        out_learnt.copyTo(analyze_toclear);
        for (i = j = 1; i < out_learnt.size(); i++){
            GClause r = reason[var(out_learnt[i])];
            if (r == GClause_NULL)
                out_learnt[j++] = out_learnt[i];
            else if (r.isLit()){
                Lit q = r.lit();
                if (!seen[var(q)] && level[var(q)] != 0)
                    out_learnt[j++] = out_learnt[i];
            }else{
                Clause& c = *r.clause();
                for (int k = 1; k < c.size(); k++)
                    if (!seen[var(c[k])] && level[var(c[k])] != 0){
                        out_learnt[j++] = out_learnt[i];
                        break; }
            }
        }
    }

    stats.max_literals += out_learnt.size();
    out_learnt.shrink(i - j);
    stats.tot_literals += out_learnt.size();

    for (int j = 0; j < analyze_toclear.size(); j++) seen[var(analyze_toclear[j])] = 0;    // ('seen[]' is now cleared)
}


// Check if 'p' can be removed. 'min_level' is used to abort early if visiting literals at a level that cannot be removed.
//
bool Solver::analyze_removable(Lit p, uint min_level)
{
    assert(reason[var(p)] != GClause_NULL);
    analyze_stack.clear(); analyze_stack.push(p);
    int top = analyze_toclear.size();
    while (analyze_stack.size() > 0){
        assert(reason[var(analyze_stack.last())] != GClause_NULL);
        GClause r = reason[var(analyze_stack.last())]; analyze_stack.pop();
        Clause& c = r.isLit() ? ((*analyze_tmpbin)[1] = r.lit(), *analyze_tmpbin)
                              : *r.clause();
        for (int i = 1; i < c.size(); i++){
            Lit p = c[i];
            if (!analyze_seen[var(p)] && level[var(p)] != 0){
                if (reason[var(p)] != GClause_NULL && ((1 << (level[var(p)] & 31)) & min_level) != 0){
                    analyze_seen[var(p)] = 1;
                    analyze_stack.push(p);
                    analyze_toclear.push(p);
                }else{
                    for (int j = top; j < analyze_toclear.size(); j++)
                        analyze_seen[var(analyze_toclear[j])] = 0;
                    analyze_toclear.shrink(analyze_toclear.size() - top);
                    return false;
                }
            }
        }
    }

    return true;
}


/*_________________________________________________________________________________________________
|
|  analyzeFinal : (confl : Clause*) (skip_first : bool)  ->  [void]
|  
|  Description:
|    Specialized analysis procedure to express the final conflict in terms of assumptions.
|    'root_level' is allowed to point beyond end of trace (useful if called after conflict while
|    making assumptions). If 'skip_first' is TRUE, the first literal of 'confl' is  ignored (needed
|    if conflict arose before search even started).
|________________________________________________________________________________________________@*/
void Solver::analyzeFinal(Clause* confl, bool skip_first)
{
    // -- NOTE! This code is relatively untested. Please report bugs!
    conflict.clear();
    if (root_level == 0) return;

    vec<char>&     seen  = analyze_seen;
    for (int i = skip_first ? 1 : 0; i < confl->size(); i++){
        Var     x = var((*confl)[i]);
        if (level[x] > 0)
            seen[x] = 1;
    }

    int     start = (root_level >= trail_lim.size()) ? trail.size()-1 : trail_lim[root_level];
    for (int i = start; i >= trail_lim[0]; i--){
        Var     x = var(trail[i]);
        if (seen[x]){
            GClause r = reason[x];
            if (r == GClause_NULL){
                assert(level[x] > 0);
                conflict.push(~trail[i]);
            }else{
                if (r.isLit()){
                    Lit p = r.lit();
                    if (level[var(p)] > 0)
                        seen[var(p)] = 1;
                }else{
                    Clause& c = *r.clause();
                    for (int j = 1; j < c.size(); j++)
                        if (level[var(c[j])] > 0)
                            seen[var(c[j])] = 1;
                }
            }
            seen[x] = 0;
        }
    }
}


/*_________________________________________________________________________________________________
|
|  enqueue : (p : Lit) (from : Clause*)  ->  [bool]
|  
|  Description:
|    Puts a new fact on the propagation queue as well as immediately updating the variable's value.
|    Should a conflict arise, FALSE is returned.
|  
|  Input:
|    p    - The fact to enqueue
|    from - [Optional] Fact propagated from this (currently) unit clause. Stored in 'reason[]'.
|           Default value is NULL (no reason).
|  
|  Output:
|    TRUE if fact was enqueued without conflict, FALSE otherwise.
|________________________________________________________________________________________________@*/
bool Solver::enqueue(Lit p, GClause from)
{
    if (value(p) != l_Undef)
        return value(p) != l_False;
    else{
        assigns[var(p)] = toInt(lbool(!sign(p)));
        level  [var(p)] = decisionLevel();
        reason [var(p)] = from;
        trail.push(p);
        return true;
    }
}


/*_________________________________________________________________________________________________
|
|  propagate : [void]  ->  [Clause*]
|  
|  Description:
|    Propagates all enqueued facts. If a conflict arises, the conflicting clause is returned,
|    otherwise NULL.
|  
|    Post-conditions:
|      * the propagation queue is empty, even if there was a conflict.
|________________________________________________________________________________________________@*/
Clause* Solver::propagate()
{
    Clause* confl = NULL;
    while (qhead < trail.size()){
        stats.propagations++;
        simpDB_props--;

        Lit            p   = trail[qhead++];     // 'p' is enqueued fact to propagate.
        vec<GClause>&  ws  = watches[index(p)];
        GClause*       i,* j, *end;

        for (i = j = (GClause*)ws, end = i + ws.size();  i != end;){
            if (i->isLit()){
                if (!enqueue(i->lit(), GClause_new(p))){
                    if (decisionLevel() == 0)
                        ok = false;
                    confl = propagate_tmpbin;
                    (*confl)[1] = ~p;
                    (*confl)[0] = i->lit();

                    qhead = trail.size();
                    // Copy the remaining watches:
                    while (i < end)
                        *j++ = *i++;
                }else
                    *j++ = *i++;
            }else{
                Clause& c = *i->clause(); i++;
                assert(c.size() > 2);
                // Make sure the false literal is data[1]:
                Lit false_lit = ~p;
                if (c[0] == false_lit)
                    c[0] = c[1], c[1] = false_lit;

                assert(c[1] == false_lit);

                // If 0th watch is true, then clause is already satisfied.
                Lit   first = c[0];
                lbool val   = value(first);
                if (val == l_True){
                    *j++ = GClause_new(&c);
                }else{
                    // Look for new watch:
                    for (int k = 2; k < c.size(); k++)
                        if (value(c[k]) != l_False){
                            c[1] = c[k]; c[k] = false_lit;
                            watches[index(~c[1])].push(GClause_new(&c));
                            goto FoundWatch; }

                    // Did not find watch -- clause is unit under assignment:
                    *j++ = GClause_new(&c);
                    if (!enqueue(first, GClause_new(&c))){
                        if (decisionLevel() == 0)
                            ok = false;
                        confl = &c;
                        qhead = trail.size();
                        // Copy the remaining watches:
                        while (i < end)
                            *j++ = *i++;
                    }
                  FoundWatch:;
                }
            }
        }
        ws.shrink(i - j);
    }

    return confl;
}


/*_________________________________________________________________________________________________
|
|  reduceDB : ()  ->  [void]
|  
|  Description:
|    Remove half of the learnt clauses, minus the clauses locked by the current assignment. Locked
|    clauses are clauses that are reason to some assignment. Binary clauses are never removed.
|________________________________________________________________________________________________@*/
struct reduceDB_lt { bool operator () (Clause* x, Clause* y) { return x->size() > 2 && (y->size() == 2 || x->activity() < y->activity()); } };
void Solver::reduceDB()
{
    int     i, j;
    double  extra_lim = cla_inc / learnts.size();    // Remove any clause below this activity

    sort(learnts, reduceDB_lt());
    for (i = j = 0; i < learnts.size() / 2; i++){
        if (learnts[i]->size() > 2 && !locked(learnts[i]))
            remove(learnts[i]);
        else
            learnts[j++] = learnts[i];
    }
    for (; i < learnts.size(); i++){
        if (learnts[i]->size() > 2 && !locked(learnts[i]) && learnts[i]->activity() < extra_lim)
            remove(learnts[i]);
        else
            learnts[j++] = learnts[i];
    }
    learnts.shrink(i - j);
}


/*_________________________________________________________________________________________________
|
|  simplifyDB : [void]  ->  [bool]
|  
|  Description:
|    Simplify the clause database according to the current top-level assigment. Currently, the only
|    thing done here is the removal of satisfied clauses, but more things can be put here.
|________________________________________________________________________________________________@*/
void Solver::simplifyDB()
{
    if (!ok) return;    // GUARD (public method)
    assert(decisionLevel() == 0);

    if (propagate() != NULL){
        ok = false;
        return; }

    if (nAssigns() == simpDB_assigns || simpDB_props > 0)   // (nothing has changed or preformed a simplification too recently)
        return;

    // Clear watcher lists:
    for (int i = simpDB_assigns; i < nAssigns(); i++){
        Lit           p  = trail[i];
        vec<GClause>& ws = watches[index(~p)];
        for (int j = 0; j < ws.size(); j++)
            if (ws[j].isLit())
                if (removeWatch(watches[index(~ws[j].lit())], GClause_new(p)))  // (remove binary GClause from "other" watcher list)
                    n_bin_clauses--;
        watches[index( p)].clear(true);
        watches[index(~p)].clear(true);
    }

    // Remove satisfied clauses:
    for (int type = 0; type < 2; type++){
        vec<Clause*>& cs = type ? learnts : clauses;
        int     j  = 0;
        for (int i = 0; i < cs.size(); i++){
            if (!locked(cs[i]) && simplify(cs[i]))  // (the test for 'locked()' is currently superfluous, but without it the reason-graph is not correctly maintained for decision level 0)
                remove(cs[i]);
            else
                cs[j++] = cs[i];
        }
        cs.shrink(cs.size()-j);
    }

    simpDB_assigns = nAssigns();
    simpDB_props   = stats.clauses_literals + stats.learnts_literals;   // (shouldn't depend on 'stats' really, but it will do for now)
}


/*_________________________________________________________________________________________________
|
|  search : (nof_conflicts : int) (nof_learnts : int) (params : const SearchParams&)  ->  [lbool]
|  
|  Description:
|    Search for a model the specified number of conflicts, keeping the number of learnt clauses
|    below the provided limit. NOTE! Use negative value for 'nof_conflicts' or 'nof_learnts' to
|    indicate infinity.
|  
|  Output:
|    'l_True' if a partial assigment that is consistent with respect to the clauseset is found. If
|    all variables are decision variables, this means that the clause set is satisfiable. 'l_False'
|    if the clause set is unsatisfiable. 'l_Undef' if the bound on number of conflicts is reached.
|________________________________________________________________________________________________@*/
lbool Solver::search(int nof_conflicts, int nof_learnts, const SearchParams& params)
{
    if (!ok) return l_False;    // GUARD (public method)
    assert(root_level == decisionLevel());

    stats.starts++;
    int     conflictC = 0;
    var_decay = 1 / params.var_decay;
    cla_decay = 1 / params.clause_decay;
    model.clear();

    for (;;){
        Clause* confl = propagate();
        if (confl != NULL){
            // CONFLICT

            stats.conflicts++; conflictC++;
            vec<Lit>    learnt_clause;
            int         backtrack_level;
            if (decisionLevel() == root_level){
                // Contradiction found:
                analyzeFinal(confl);
                return l_False; }
            analyze(confl, learnt_clause, backtrack_level);
            cancelUntil(max(backtrack_level, root_level));
            newClause(learnt_clause, true);
            if (learnt_clause.size() == 1) level[var(learnt_clause[0])] = 0;    // (this is ugly (but needed for 'analyzeFinal()') -- in future versions, we will backtrack past the 'root_level' and redo the assumptions)
            varDecayActivity();
            claDecayActivity();

        }else{
            // NO CONFLICT

            if (nof_conflicts >= 0 && conflictC >= nof_conflicts){
                // Reached bound on number of conflicts:
                progress_estimate = progressEstimate();
                cancelUntil(root_level);
                return l_Undef; }

            if (decisionLevel() == 0)
                // Simplify the set of problem clauses:
                simplifyDB(), assert(ok);

            if (nof_learnts >= 0 && learnts.size()-nAssigns() >= nof_learnts)
                // Reduce the set of learnt clauses:
                reduceDB();

            // New variable decision:
            stats.decisions++;
            Var next = order.select(params.random_var_freq);

            if (next == var_Undef){
                // Model found:
                model.growTo(nVars());
                for (int i = 0; i < nVars(); i++) model[i] = value(i);
                cancelUntil(root_level);
                return l_True;
            }

            check(assume(~Lit(next)));
        }
    }
}


// Return search-space coverage. Not extremely reliable.
//
double Solver::progressEstimate()
{
    double  progress = 0;
    double  F = 1.0 / nVars();
    for (int i = 0; i < nVars(); i++)
        if (value(i) != l_Undef)
            progress += pow(F, level[i]);
    return progress / nVars();
}


// Divide all variable activities by 1e100.
//
void Solver::varRescaleActivity()
{
    for (int i = 0; i < nVars(); i++)
        activity[i] *= 1e-100;
    var_inc *= 1e-100;
}


// Divide all constraint activities by 1e100.
//
void Solver::claRescaleActivity()
{
    for (int i = 0; i < learnts.size(); i++)
        learnts[i]->activity() *= 1e-20;
    cla_inc *= 1e-20;
}


/*_________________________________________________________________________________________________
|
|  solve : (assumps : const vec<Lit>&)  ->  [bool]
|  
|  Description:
|    Top-level solve. If using assumptions (non-empty 'assumps' vector), you must call
|    'simplifyDB()' first to see that no top-level conflict is present (which would put the solver
|    in an undefined state).
|________________________________________________________________________________________________@*/
bool Solver::solve(const vec<Lit>& assumps)
{
    simplifyDB();
    if (!ok) return false;

    SearchParams    params(default_params);
    double  nof_conflicts = 100;
    double  nof_learnts   = nClauses() / 3;
    lbool   status        = l_Undef;

    // Perform assumptions:
    root_level = assumps.size();
    for (int i = 0; i < assumps.size(); i++){
        Lit p = assumps[i];
        assert(var(p) < nVars());
        if (!assume(p)){
            GClause r = reason[var(p)];
            if (r != GClause_NULL){
                Clause* confl;
                if (r.isLit()){
                    confl = propagate_tmpbin;
                    (*confl)[1] = ~p;
                    (*confl)[0] = r.lit();
                }else
                    confl = r.clause();
                analyzeFinal(confl, true);
                conflict.push(~p);
            }else
                conflict.clear(),
                conflict.push(~p);
            cancelUntil(0);
            return false; }
        Clause* confl = propagate();
        if (confl != NULL){
            analyzeFinal(confl), assert(conflict.size() > 0);
            cancelUntil(0);
            return false; }
    }
    assert(root_level == decisionLevel());

    // Search:
    if (verbosity >= 1){
        reportf("==================================[MINISAT]===================================\n");
        reportf("| Conflicts |     ORIGINAL     |              LEARNT              | Progress |\n");
        reportf("|           | Clauses Literals |   Limit Clauses Literals  Lit/Cl |          |\n");
        reportf("==============================================================================\n");
    }

    while (status == l_Undef){
        if (verbosity >= 1)
            reportf("| %9d | %7d %8d | %7d %7d %8d %7.1f | %6.3f %% |\n", (int)stats.conflicts, nClauses(), (int)stats.clauses_literals, (int)nof_learnts, nLearnts(), (int)stats.learnts_literals, (double)stats.learnts_literals/nLearnts(), progress_estimate*100);
        status = search((int)nof_conflicts, (int)nof_learnts, params);
        nof_conflicts *= 1.5;
        nof_learnts   *= 1.1;
    }
    if (verbosity >= 1)
        reportf("==============================================================================\n");

    cancelUntil(0);
    return status == l_True;
}
