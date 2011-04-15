#pragma comment(linker, "/STACK:16777216")
#define _CRT_SECURE_NO_WARNINGS
#include <algorithm>
#include <iostream>
#include <iomanip>
#include <sstream>
#include <string>
#include <vector>
#include <queue>
#include <set>
#include <map>
#include <cstdio>
#include <cstdlib>
#include <cctype>
#include <cmath>
#include <climits>
#include <numeric>
#include <memory.h>
//#include <ctime>
//clock_t startTime=clock();
//fprintf(stderr,"time=%.3lfsec\n",0.001*(clock()-startTime));
using namespace std;
 
typedef long long ll;
typedef vector<int> vi;
typedef vector<vi> vvi;
typedef vector<ll> vl;
typedef vector<vl> vvl;
typedef vector<bool> vb;
typedef vector<vb> vvb;
typedef vector<double> vd;
typedef vector<vd> vvd;
typedef vector<string> vs;
typedef pair<int, int> pii;
typedef vector<pii> vpii;
typedef pair<double, double> pdd;
typedef vector<pdd> vpdd;
typedef vector<vpii> Graph;
 
#define fr(i,a,b) for(int i(a),_b(b);i<=_b;++i)
#define frd(i,a,b) for(int i(a),_b(b);i>=_b;--i)
#define rep(i,n) for(int i(0),_n(n);i<_n;++i)
#define reps(i,a) fr(i,0,sz(a)-1)
#define all(a) a.begin(),a.end()
#define pb push_back
#define mp make_pair
#define clr(x,a) memset(x,a,sizeof(x))
#define findx(a,x) (find(all(a),x)-a.begin())
#define two(X) (1LL<<(X))
#define contain(S,X) (((S)&two(X))!=0)
 
const int dr[]={0,-1,0,1,-1,1, 1,-1};
const int dc[]={1,0,-1,0, 1,1,-1,-1};
const double eps=1e-9;
 
template<class T> void print(const vector<T> &v){ostringstream os; for(int i=0; i<v.size(); ++i){if(i)os<<' ';os<<v[i];} cout<<os.str()<<endl;}
template<class T> int sz(const T&c){return (int)c.size();}
template<class T> void srt(T&c){sort(c.begin(),c.end());}
template<class T> void checkmin(T &a,T b){if(b<a) a=b;}
template<class T> void checkmax(T &a,T b){if(b>a) a=b;}
template<class T> T sqr(T x){return x*x;}
template<class T, class U> T cast (U x) { ostringstream os; os<<x; T res; istringstream is(os.str()); is>>res; return res; }
template<class T> vector<T> split(string s, string x=" ") {vector<T> res; for(int i=0;i<s.size();i++){string a; while(i<(int)s.size()&&x.find(s[i])==string::npos)a+=s[i++]; if(!a.empty())res.push_back(cast<T>(a));} return res;}
template<class T> bool inside(T r,T c,T R, T C){return r>=0 && r<R && c>=0 && c<C;}
 
int minu=-1;
 
void dfs1(vvi &g,const vi &p,int u, vb &vis){
	if(minu==-1 || p[minu]>p[u])minu=u;
	vis[u/2]=true;
	reps(i,g[u]){
		int v=g[u][i];
		if(minu==-1 || p[minu]>p[v])minu=v;
		if(vis[v/2])continue;
		dfs1(g,p,v,vis);
	}
	u=u^1;
	if(u<sz(g)){
		if(minu==-1 || p[minu]>p[u])minu=u;
		reps(i,g[u]){
			int v=g[u][i];
			if(minu==-1 || p[minu]>p[v])minu=v;
			if(vis[v/2])continue;
			dfs1(g,p,v,vis);
		}
	}
}
 
void dfs(vvi &g,string &s,int u, vb &vis){
	vis[u/2]=true;
	reps(i,g[u]){
		int v=g[u][i];
		if(vis[v/2])continue;
		if(s[u]==s[v])swap(s[v],s[v^1]);
		dfs(g,s,v,vis);
	}
	u=u^1;
	if(u<sz(g))
	reps(i,g[u]){
		int v=g[u][i];
		if(vis[v/2])continue;
		if(s[u]==s[v])swap(s[v],s[v^1]);
		dfs(g,s,v,vis);
	}
}
 
string solve(const vi &a, const vi &b){
	int n=sz(a);
	vi p(n);
	rep(i,n)p[a[i]]=i;
	vi c(n);
	rep(i,n)c[i]=p[b[i]];
	string s=string(n+n%2, 'A');
	rep(i,(n+1)/2)s[2*i+1]='B';
 
	vvi g(n);
	rep(i,n/2){
		int x=c[2*i];
		int y=c[2*i+1];
		g[x].pb(y);
		g[y].pb(x);
	}
	vb vis((n+1)/2);
	vb vis1((n+1)/2);
	rep(i,n){
		if(!vis[i/2]){
			minu=-1;
			dfs1(g,a,i,vis1);
			if((minu^1)<n && (a[minu]<a[minu^1] ^ s[minu]<s[minu^1]))swap(s[minu],s[minu^1]);
			dfs(g,s,minu,vis);
		}
	}
 
	string t=string(n,' ');
	rep(i,n)t[a[i]]=s[i];
	return t;
}
 
int main( int argc, char* argv[] ) {
	#ifndef ONLINE_JUDGE
	freopen("input.txt","r",stdin);
	//freopen("output.txt","w",stdout);
	#endif	
 
	int tc;
	scanf("%d", &tc);
	//tc=1000;
	while(tc--){
		int n;
		scanf("%d", &n);
		vi a(n);
		rep(i,n)scanf("%d", &a[i]);
		vi b(n);
		rep(i,n)scanf("%d", &b[i]);
 
		/*
		n=rand()%2000+1;
		//n=20000;
		vi a(n),b(n);
		rep(i,n)a[i]=b[i]=i;
		random_shuffle(all(a));
		random_shuffle(all(b));
		*/
 
		string t=solve(a,b);
 
		int z1=0,z2=0;
		int a1=0,b1=0;
		int a2=0,b2=0;
		rep(i,n){
			a1+=t[a[i]]=='A';
			b1+=t[a[i]]=='B';
			a2+=t[b[i]]=='A';
			b2+=t[b[i]]=='B';
			checkmax(z1,abs(a1-b1));
			checkmax(z2,abs(a2-b2));
		}
 
		
		if(z1>1||z2>1){
		print(a);
		print(b);
		cout<<"       "<<z1<<" "<<z2<<endl;
		exit(0);
		}
		
		cout<<t<<endl;
		//cout<<"       "<<z1<<" "<<z2<<endl;
	}
	//cout<<"ok"<<endl;
 
	return 0;
}
