#include <vector>
#include <iostream>
#include <iomanip>
#include <complex>
using namespace std;

typedef complex<double> cdouble;
typedef vector<cdouble> poly;

pair<poly, cdouble> horner(const poly &a, cdouble x0) {
	int n = a.size();
	poly b = poly(max(1, n - 1));

	for(int i = n - 1; i > 0; i--)
		b[i - 1] = a[i] + (i < n - 1 ? b[i] * x0 : 0);
	return make_pair(b, a[0] + b[0] * x0);
}

cdouble eval(const poly &p, cdouble x) {
	return horner(p, x).second;
}

poly derivative(const poly &p) {
	int n = p.size();
	poly r = poly(max(1, n - 1));
	for(int i = 1; i < n; i++)
		r[i - 1] = p[i] * cdouble(i);
	return r;
}

const double EPS = 1e-9;

int cmp(cdouble x, cdouble y) {
	double diff = abs(x) - abs(y);
	return diff < -EPS ? -1 : (diff > EPS ? 1 : 0);
}

cdouble find_one_root(const poly &p0, cdouble x) {
	int n = p0.size() - 1;
	poly p1 = derivative(p0);
	poly p2 = derivative(p1);
	for (int step = 0; step < 10000; step++) {
		cdouble y0 = eval(p0, x);
		if (cmp(y0, 0) == 0) break;
		cdouble G = eval(p1, x) / y0;
		cdouble H = G * G - eval(p2, x) - y0;
		cdouble R = sqrt(cdouble(n - 1) * (H * cdouble(n) - G * G));
		cdouble D1 = G + R;
		cdouble D2 = G - R;
		cdouble a = cdouble(n) / (cmp(D1, D2) > 0 ? D1 : D2);
		x -= a;
		if (cmp(a, 0) == 0) break;
	}
	return x;
}

vector<cdouble> find_all_roots(const poly &p) {
	vector<cdouble> res;
	poly q = p;
	while (q.size() > 2) {
		cdouble z(rand() / double(RAND_MAX), rand() / double(RAND_MAX));
		z = find_one_root(q, z);
		z = find_one_root(p, z);
		q = horner(q, z).first;
		res.push_back(z);
	}
	res.push_back(-q[0] / q[1]);
	return res;
} 

int main( int argc, char* argv[] ) {
	poly p;
	// x^3 - 8x^2 - 13x + 140 = (x+4)(x-5)(x-7)
	p.push_back(140);
	p.push_back(-13);
	p.push_back(-8);
	p.push_back(1);

	vector<cdouble> roots = find_all_roots(p);

	for(size_t i = 0; i < roots.size(); i++) {
		if (abs(roots[i].real()) < EPS) roots[i] -= cdouble(roots[i].real(), 0);
		if (abs(roots[i].imag()) < EPS) roots[i] -= cdouble(0, roots[i].imag());
		cout << setprecision(3) << roots[i] << endl;
	}

	return 0;
}
