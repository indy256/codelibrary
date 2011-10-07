/**************************************************************************************[VarOrder.h]
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

#ifndef VarOrder_h
#define VarOrder_h

#include "SolverTypes.h"
#include "Heap.h"


//=================================================================================================


struct VarOrder_lt {
    const vec<double>&  activity;
    bool operator () (Var x, Var y) { return activity[x] > activity[y]; }
    VarOrder_lt(const vec<double>&  act) : activity(act) { }
};

class VarOrder {
    const vec<char>&    assigns;     // var->val. Pointer to external assignment table.
    const vec<double>&  activity;    // var->act. Pointer to external activity table.
    Heap<VarOrder_lt>   heap;
    double              random_seed; // For the internal random number generator

public:
    VarOrder(const vec<char>& ass, const vec<double>& act) :
        assigns(ass), activity(act), heap(VarOrder_lt(act)), random_seed(91648253)
        { }

    inline void newVar(void);
    inline void update(Var x);                  // Called when variable increased in activity.
    inline void undo(Var x);                    // Called when variable is unassigned and may be selected again.
    inline Var  select(double random_freq =.0); // Selects a new, unassigned variable (or 'var_Undef' if none exists).
};


void VarOrder::newVar(void)
{
    heap.setBounds(assigns.size());
    heap.insert(assigns.size()-1);
}


void VarOrder::update(Var x)
{
    if (heap.inHeap(x))
        heap.increase(x);
}


void VarOrder::undo(Var x)
{
    if (!heap.inHeap(x))
        heap.insert(x);
}


Var VarOrder::select(double random_var_freq)
{
    // Random decision:
    if (drand(random_seed) < random_var_freq && !heap.empty()){
        Var next = irand(random_seed,assigns.size());
        if (toLbool(assigns[next]) == l_Undef)
            return next;
    }

    // Activity based decision:
    while (!heap.empty()){
        Var next = heap.getmin();
        if (toLbool(assigns[next]) == l_Undef)
            return next;
    }

    return var_Undef;
}


//=================================================================================================
#endif
