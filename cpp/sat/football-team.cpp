// =====================================================================================
//   [ Filename    ]  pcsmall.cpp
//   [ Description ]  
//   [ Created     ]  10/11/2009 01:26:13 AM CST
//   [ Author      ]  Jiunru Yang , yangjiunru [at] gmail.com, NTUEE
// =====================================================================================

#include <iostream>
#include "Solver.h"


using namespace std;

class Sol
{
   public:
      void read() {
         cin >> N;
         for (int i = 0; i < N; ++i) {
            cin >> x[i] >> y[i];
         }
      }

      int vn(int nc, int player, int c) {
         return 2*(nc * player + c);
      }

      void formulate(int nc, Solver& s) {
         for (int i = 0; i < N; ++i)
            for (int j = 0; j < nc; ++j)
               s.newVar();
         for (int i = 0; i < N; ++i) {
            vec<Lit> c;
            c.growTo(nc);
            for (int j = 0; j < nc; ++j)
               c[j] = toLit(vn(nc, i, j));
            s.addClause(c);
            if (nc > 1) {
               for (int j = 0; j < nc; ++j)
                  for (int k = j + 1; k < nc; ++k)
                     s.addBinary(~toLit(vn(nc, i, j)), ~toLit(vn(nc, i, k)));
            }
         }
         for (int i = 0; i < N; ++i) {
            for (int py = y[i] - 1; py <= y[i] + 1; ++py) {
               int min = 10000;
               int which = 0;
               for (int j = 0; j < N; ++j) {
                  if (y[j] != py) continue;
                  if (x[j] < min && x[j] > x[i]) {
                     min = x[j];
                     which = j;
                  }
               }
               if (min == 10000) continue;
               for (int j = 0; j < nc; ++j)
                  s.addBinary(~toLit(vn(nc, i, j)), ~toLit(vn(nc, which, j)));
            }
         }
      }

      void solve(int caseNo) {
         read();
         for (int nc = 1; nc <= 100; ++nc) {
            Solver s;
            formulate(nc, s);
            if (s.solve() && s.okay()) {
               cout << "Case #" << caseNo << ": " << nc << endl;
               return;
            }
         }
         cout << "error" << endl;
      }

      int N;
      int x[1000], y[1000];
};

int main()
{
   int T;
   cin >> T;
   for (int t = 1; t <= T; ++t) {
      Sol s;
      s.solve(t);
   }
   return 0;
}
