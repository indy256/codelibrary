#include <sstream>
#include <iostream>
#include "Solver.h"
#include <vector>
#include <map>
#include <cassert>

using namespace std;


class Sol
{
   public:
   void read()
   {
      vector<string> a;
      cin >> N;
      a.resize(2*N);
      d.resize(N);
      for (int i = 0; i < 2*N; ++i) {
         cin >> a[i];
      }
      map<string,unsigned> m;
      unsigned c = 0;
      for (int i = 0; i < 2*N; ++i) {
         if (m.find(a[i]) == m.end())
            m[a[i]] = c++;
      }
      for (int i = 0; i < 2*N; ++i) {
         d[m[a[i]]].push_back(i);
      }
   }

   void formulate(int p, int n, Solver& s) {
      _p = p; _n = n;
      for (int i = 0; i < N*(p+n); ++i)
         s.newVar();
      for (int i = 0; i < N; ++i)
         for (int j = 0; j < i; ++j) {
            if (intersect(i, j)) {
               for (int r = 1; r <= p; ++r) {
                  Lit a = vn(i, r);
                  Lit b = vn(j, r);
                  s.addBinary(~a, ~b);
               }
               for (int r = -1; r >= -n; --r) {
                  Lit a = vn(i, r);
                  Lit b = vn(j, r);
                  s.addBinary(~a, ~b);
               }
            }
         }
      for (int i = 0; i < N; ++i) {
         for (int j = 0; j < N; ++j) {
            if (i == j) continue;
            if (!contain(i, j)) continue;
            for (int pj = 1; pj <= p-1; ++pj) {
               for (int pi = pj+1; pi <= p; ++pi) {
                  Lit a = vn(i, pi);
                  Lit b = vn(j, pj);
                  s.addBinary(~a, ~b);
               }
            }
            for (int pj = -1; pj >= -n+1; --pj) {
               for (int pi = pj-1; pi >= -n; --pi) {
                  Lit a = vn(i, pi);
                  Lit b = vn(j, pj);
                  s.addBinary(~a, ~b);
               }
            }
         }
      }

      // Exact one for each color
      /*vec<Lit>* c;
      for (int i = 0; i < N; ++i) {
         c = new vec<Lit>;
         c->growTo(n+p);
         for (int pi = 1; pi <= p; ++pi)
            (*c)[i-1] = vn(i, pi);
         for (int pi = -1; pi >= -n; --pi)
            (*c)[p-pi-1] = vn(i, pi);
         s.addClause(*c);
         for (int j = 0; j < n+p; ++j)
            for (int k = 0; k < j; ++k) {
               s.addBinary(~vnalt(i, j), ~vnalt(i, k));
            }
      }*/
      for (int i = 0; i < N; ++i) {
         vec<Lit> c;
         c.growTo(n+p);
         for (int j = 0; j < n+p; ++j)
            c[j] = vnalt(i, j);
         s.addClause(c);
      }
      for (int i = 0; i < N; ++i) {
         for (int j = 0; j < n+p; ++j)
            for (int k = 0; k < j; ++k) {
               s.addBinary(~vnalt(i, j), ~vnalt(i, k));
            }
      }
   }

   bool check(int h) {
      for (int i = 0; i <= h; ++i) {
         Solver s;
         formulate(i, h-i, s);
         if (s.solve() && s.okay())
            return true;
      }
      return false;
   }

   void solve(int caseNo) {
      int i = 1;
      while (!check(i)) {
         ++i;
         if (i >= N*2 + 10) {
            i = -1;
            break;
         }
      }
      cout << "Case #" << caseNo << ": " << i << endl;
   }

   bool contain(int i, int j) { // A point of i is contained in j
      if (d[i][0] >= d[j][0] && d[i][0] <= d[j][1])
         return true;
      if (d[i][1] >= d[j][0] && d[i][1] <= d[j][1])
         return true;
      return false;
   }

   bool intersect(int i, int j) {
      return !(d[i][0] > d[j][1] || d[i][1] < d[j][0]);
   }

   Lit vn(int i, int pnn) {
      assert(((_n+_p) * i + pnn + _n - (pnn>0?1:0)) < N * (_p + _n));
      return toLit(2*((_n+_p) * i + pnn + _n - (pnn>0?1:0)));
   }

   Lit vnalt(int i, int j) {
      assert(((_n+_p)*i + j) < N * (_p + _n));
      return toLit(2*((_n+_p)*i + j));
   }

   int N;
   int _p, _n;
   vector<vector<unsigned> > d;
};

int main()
{
   int T;
   cin >> T;
   for (int t = 1; t <= T; ++t) {
      Sol s;
      s.read();
      s.solve(t);
   }
   return 0;
}
