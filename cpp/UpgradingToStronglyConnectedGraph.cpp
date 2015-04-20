#include <cstdio>
#include <vector>
using namespace std;

#define MAX 12000
bool in[MAX],  // in[c] == true  означает, что есть входящие рёбра в с.к. c
     out[MAX], // out[c] == true  означает, что есть исходящие рёбра из с.к. c
     dead[MAX]; 

vector<int> s[MAX],     // s[v] = множество смежных вершин вершины v.
            s_rev[MAX], // s_rev[v] = множество смежных вершин вершины v 
                        //           в транспонированном графе.
            q,          // множество всех вершин в порядке роста времени выхода из них
                        // при обходе графа в глубину.
            m_a, m_b;   // 

int c[MAX],
    cur_c,
    next_s[MAX],
    vis[MAX],
    next_vis;

/* Обход в глубину всех вершин, достижимых из вершины x.
 * В вектор q они будут помещены в порядке возрастания "времени выхода".
 */
void dfs(int x) {
  vis[x] = next_vis; // "закрашиваем" эту вершину "цветом" next_vis
  for (int i = 0; i < s[x].size(); i++) 
    if ( !vis[s[x][i]] ) dfs( s[x][i] );
  q.push_back(x);
}

/* Обход в глубину всех вершин, достижимых из вершины x в
 * транспонированном графе.
 */
void dfs_rev(int x) {
  c[x] = cur_c; // "закрашиваем" эту вершину "цветом" cur_c
  for (int i = 0; i < s_rev[x].size(); i++) 
    if ( !c[s_rev[x][i]] ) 
       dfs_rev(s_rev[x][i]);
}

bool match(int x) {
  if ( dead[x] || vis[x] == next_vis ) return false;

  vis[x] = next_vis;

  if ( !out[ c[x] ] ) {
    m_b.push_back(x);
    out[ c[x] ] = true;
    return true;
  }

  while ( next_s[x] < s[x].size() && !match( s[x][next_s[x]] ) ) 
    next_s[x]++;
  return !(dead[x] = next_s[x] == s[x].size());
}

int main() {
  int n, // число вершин 
      m, // число рёбер
      i, j, a = 0, b = 0;
  
  // Считываем входные данные
  scanf("%d %d", &n, &m);
  while ( m-- ) {
    scanf("%d %d", &i, &j);
    s[i].push_back(j);
    s_rev[j].push_back(i);
  }

  // Обходим все вершины методом "в глубину". 
  // И помещаем их в вектор q в порядке выхода из них рекурсивной процедуры обхода. 
  next_vis = 1; 
  for (i = 0; i < n; i++) if ( !vis[i] ) dfs(i);

  // Обходим все вершины методом "в глубину" в транспонированном графе. 
  // Берем вершины в порядке уменьшения времени "выхода из них" 
  // во время первого обхода.
  // Цвет c[v] вершины v есть номер связной компоненты.
  for (i = n - 1; i >= 0; i--)
    if ( !c[q[i]] ) { cur_c++; dfs_rev(q[i]); }

  // Находим связные компоненты в которые что-то входит
  // и связные компоненты из которых что-то выходит в другие компоненты.
  for (i = 0; i < n; i++)
    for (j = 0; j < s[i].size(); j++)
      if (c[i] != c[s[i][j]])
        out[c[i]] = in[c[s[i][j]]] = true;

  if (cur_c == 1) { printf("0\n"); return 0; }

  for (i = 1; i <= cur_c; i++) { a += !out[i]; b += !in[i]; }
  printf("%d\n", a >? b); //  Бинарный оператор "a >? b" есть max(a,b).

  for (i = 0; i < n; i++) {
    next_vis++;
    if ( !in[c[i]] && !dead[i] && match(i) ) {
      m_a.push_back(i);
      in[ c[i] ] = true;
    }
  }

  for (i = 0; i < m_a.size(); i++ ) 
    printf("%d %d\n", m_b[i], m_a[(i+1)%m_a.size()] );

  vector<int> A, B;
  for (i = 0; i < n; i++) {
    if ( !in[c[i]] ) A.push_back(i);
    in[c[i]] = true;
  }

  for (i = 0; i < n; i++) {
    if (!out[c[i]]) B.push_back(i);
    out[c[i]] = true;
  }

  while ( A.size() && B.size() ) {
    printf("%d %d\n", B.back(), A.back());
    A.pop_back(); B.pop_back();
  }

  while ( A.size() ) {
    printf("%d %d\n", m_b.back(), A.back());
    A.pop_back();
  }

  while ( B.size() ) {
    printf("%d %d\n", B.back(), m_a.back());
    B.pop_back();
  }
  
  return 0;
}