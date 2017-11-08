binarySearchFirstTrue :: (Int -> Bool) -> Int -> Int -> Int
binarySearchFirstTrue predicate lo hi
  | lo > hi = lo
  | predicate mid = binarySearch predicate lo mid - 1
  | otherwise = binarySearch predicate mid + 1 hi
  where mid = (lo + hi) `div` 2

-- Usage example
main = print $ binarySearchFirstTrue (\x -> x >= 5) 0 10
