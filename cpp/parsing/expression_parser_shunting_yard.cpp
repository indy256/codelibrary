#include <bits/stdc++.h>

using namespace std;

stack<int> values;
stack<char> ops;

void process_op() {
    int r = values.top();
    values.pop();
    int l = values.top();
    values.pop();
    char op = ops.top();
    ops.pop();
    switch (op) {
        case '+':
            values.push(l + r);
            break;
        case '-':
            values.push(l - r);
            break;
        case '*':
            values.push(l * r);
            break;
        case '/':
            values.push(l / r);
            break;
    }
}

int priority(char op) {
    switch (op) {
        case '+':
        case '-':
            return 1;
        case '*':
        case '/':
            return 2;
    }
    return 0;
}

int eval(string s) {
    s = '(' + s + ')';
    for (char c : s)
        if ('0' <= c && c <= '9')
            values.push(c - '0');
        else if (c == '(')
            ops.push(c);
        else if (c == ')') {
            while (ops.top() != '(')
                process_op();
            ops.pop();
        } else {
            while (!ops.empty() && priority(ops.top()) >= priority(c))
                process_op();
            ops.push(c);
        }
    return values.top();
}

// usage example
int main() {
    cout << eval("2*2+2") << endl;
    cout << eval("2+2*2") << endl;
    cout << eval("(3+2)*2") << endl;
}
