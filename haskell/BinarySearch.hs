binarySearchFirstTrue :: (Int -> Bool) -> Int -> Int -> Int
binarySearchFirstTrue predicate lo hi
  | hi - lo <= 1 = hi
  | predicate mid = binarySearch predicate lo mid
  | otherwise = binarySearch predicate mid hi
  where mid = (lo + hi) `div` 2

-- Usage example
main = print $ binarySearchFirstTrue (\x -> x >= 5) 0 10
