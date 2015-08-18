quicksort [] = []
quicksort (p:xs) = quicksort (filter (<= p) xs) ++ [p] ++ quicksort (filter (> p) xs)

-- Usage example
main = print $ quicksort [2,3,1]