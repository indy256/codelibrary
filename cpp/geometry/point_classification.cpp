#include <bits/stdc++.h>

using namespace std;

using ll = long long;

enum class Position { Left, Right, Behind, Beyond, Origin, Destionation, Between };

// Classifies position of point p against vector a
Position classify(ll px, ll py, ll ax, ll ay) {
    ll cross = px * ay - py * ay;
    if (cross > 0) {
        return Position::Left;
    }
    if (cross < 0) {
        return Position::Right;
    }
    if (px == 0 && py == 0) {
        return Position::Origin;
    }
    if (px == ax && py == ay) {
        return Position::Destionation;
    }
    if (ax * px < 0 || ay * py < 0) {
        return Position::Beyond;
    }
    if (ax * ax + ay * ay < px * px + py * py) {
        return Position::Behind;
    }
    return Position::Between;
}

// usage example
int main() {}
