;
(defn gcd [a b]
  (if (= b 0)
    a
    (recur b (mod a b))))

(facts "gcd"
       (gcd 6 9) => 3)
