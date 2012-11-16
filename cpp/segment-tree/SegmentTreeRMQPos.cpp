const int maxn = 200000;
int tminPos[4 * maxn];
int t[maxn];

void buildTree(int node = 1, int left = 0, int right = maxn - 1) {
	if (left == right) {
		tminPos[node] = t[left];
	} else {
		int mid = (left + right) >> 1;
		buildTree(node * 2, left, mid);
		buildTree(node * 2 + 1, mid + 1, right);
		tminPos[node] = t[tminPos[node * 2]] <= t[tminPos[node * 2 + 1]] ? tminPos[node * 2] : tminPos[node * 2 + 1];
	}
}

void setv(int i, int value, int node = 1, int left = 0, int right = maxn-1) {
	if (left > i || right < i)
		return;
	if (left == right) {
		t[left] = value;
		return;
	}
	int mid = (left + right) >> 1;
	setv(i, value, node * 2, left, mid);
	setv(i, value, node * 2 + 1, mid + 1, right);
	tminPos[node] = t[tminPos[node * 2]] <= t[tminPos[node * 2 + 1]] ? tminPos[node * 2] : tminPos[node * 2 + 1];
}

int minPos(int a, int b, int node = 1, int left = 0, int right = maxn - 1) {
	if (left > b || right < a)
		return -1;
	if (left >= a && right <= b)
		return tminPos[node];
	int mid = (left + right) >> 1;
	int p1 = minPos(a, b, node * 2, left, mid);
	int p2 = minPos(a, b, node * 2 + 1, mid + 1, right);
	if (p1 == -1)
		return p2;
	if (p2 == -1)
		return p1;
	return t[tminPos[p1]] <= t[tminPos[p2]] ? p1 : p2;
}
