import sys

arr = [1,2,4,-1,3]

def bubblesort(arr):
	for i in range(1, len(arr)-1):
		for j in range(len(arr)-i):
			if arr[j] > arr[j+1]:
				arr[j], arr[j+1] = arr[j+1], arr[j]
	return arr

if __name__ == '__main__':
	print(bubblesort(sys.argv[1:]))

