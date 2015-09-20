quicksort [] = []
quicksort (p:a) = quicksort (filter (<= p) a) ++ [p] ++ quicksort (filter (> p) a)

-- Usage example
main = print $ quicksort [2,3,1]
