#include <stdio.h>
#include <stdlib.h>

// returns index of element in array if exists
// returns -1 if element not exists
int binsearch(int* arr, int size, int x) {
	int l = 0;
	int r = size - 1;
	do {
		int mid = l + (r-l)/2;
		printf("left: %d, right: %d, mid: %d\n", l, r, mid);
		if (arr[mid] == x) return mid;
		else if (arr[mid] > x) r = mid - 1;
		else if (arr[mid] < x) l = mid + 1;
	} while (l <= r);
	return -1;
}
int main(int argc, char **argv) {
	int *arr = (int*)calloc(argc, sizeof(int));
	for (int i = 0; i < argc-1; i++) {
		arr[i] = atoi(argv[i+1]);
		printf("%d ", arr[i]);
	}
	printf("\n");

	int x = 3;
	printf("index of %d is : %d\n", x, binsearch(arr, argc - 1, x));
	return 0;
}
