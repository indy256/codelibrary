#include <cstdio>
#include <memory.h>

const int BUF_SIZE = 65536;
char input[BUF_SIZE];

struct scanner {
    char* curPos;

    scanner() {
        fread(input, 1, sizeof(input), stdin);
        curPos = input;
    }

    void ensureCapacity() {
        int size = input + BUF_SIZE - curPos;
        if (size < 100) {
            memcpy(input, curPos, size);
            fread(input + size, 1, BUF_SIZE - size, stdin);
            curPos = input;
        }
    }

    int nextInt() {
        ensureCapacity();
        while (*curPos <= ' ')
            ++curPos;
        bool sign = false;
        if (*curPos == '-') {
            sign = true;
            ++curPos;
        }
        int res = 0;
        while (*curPos > ' ')
            res = res * 10 + (*(curPos++) & 15);
        return sign ? -res : res;
    }

    char nextChar() {
        ensureCapacity();
        while (*curPos <= ' ')
            ++curPos;
        return *(curPos++);
    }
};

int main() {
    scanner sc;
    int a = sc.nextInt();
    char b = sc.nextChar();

    printf("%d %c\n", a, b);
}
