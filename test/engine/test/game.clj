(ns engine.test.game
  (:use [lazytest.describe :only (describe it)])
  (:use engine.game))

(def fixture [1 1])
(def no-neighbour #{fixture})
(def one-neighbour #{fixture [0 1]})
(def two-neighbours #{fixture [0 1] [1 2]})
(def three-neighbours #{fixture [0 1] [1 2] [1 0]})
(def four-neighbours #{fixture [0 1] [1 2] [1 0] [0 0]})

(def left-neighbour #{fixture [0 1]})
(def right-neighbour #{fixture [2 1]})
(def upper-neighbour #{fixture [1 0]})
(def lower-neighbour #{fixture [1 2]})
(def upper-left-neighbour #{fixture [0 0]})
(def upper-right-neighbour #{fixture [2 0]})
(def lower-left-neighbour #{fixture [0 2]})
(def lower-right-neighbour #{fixture [2 2]})

(defn alive?
  [cell field]
   (contains? (set field) cell)) 

(defn dead?
  [cell field]
  (not (alive? cell field)))


(describe "blinker should"
  (it "oscillate"
    (= blinker (tick (tick blinker)))))

(describe "box should"
  (it "not change"
    (= box (tick (tick box)))))

(describe "boat should"
  (it "not change"
    (= boat (tick (tick boat)))))

(describe "regulate should"
  (it "remove cells with no neighbour"
    (dead? fixture (regulate no-neighbour)))
  (it "remove cells with one neighbour"
    (dead? fixture (regulate one-neighbour)))
  (it "keep cells with two neighbours"
    (alive? fixture (regulate two-neighbours)))
  (it "keep cells with three neighbours"
    (alive? fixture (regulate three-neighbours)))
  (it "remove cells with four neighbours"
    (dead? fixture (regulate four-neighbours)))
  (it "keep one cell of blinker"
    (alive? [3 2]  (regulate blinker))))

(describe "alive-neighbours should return"
  (it "left neighbour"
    (= '([0 1]) (alive-neighbours left-neighbour fixture)))
  (it "right neighbour"
    (= '([2 1]) (alive-neighbours right-neighbour fixture)))
  (it "upper neighbour"
    (= '([1 0]) (alive-neighbours upper-neighbour fixture)))
  (it "lower neighbour"
    (= '([1 2]) (alive-neighbours lower-neighbour fixture)))
  (it "upper-left neighbour"
    (= '([0 0]) (alive-neighbours upper-left-neighbour fixture)))
  (it "upper-right neighbour"
    (= '([2 0]) (alive-neighbours upper-right-neighbour fixture)))
  (it "lower-left neighbour"
    (= '([0 2]) (alive-neighbours lower-left-neighbour fixture)))
  (it "lower-right neighbour"
    (= '([2 2]) (alive-neighbours lower-right-neighbour fixture)))
  (it "two neighbours"
    (= '([0 1] [1 2]) (alive-neighbours two-neighbours fixture)))
  (it "three neighbours"
    (= '((0 1) (1 0) (1 2)) (alive-neighbours three-neighbours fixture)))
  (it "four neighbours"
    (= '((0 0) (0 1) (1 0) (1 2)) (alive-neighbours four-neighbours fixture))))


(describe "reproduce should"
  (it "create cell for three neighbours"
    (alive? [1 1] (reproduce #{[0 0] [1 0] [0 1]})))
  (it "create no cell for two neighbours"
    (dead? [1 1] (reproduce #{[0 0] [1 0] })))
  (it "create no cell for one neighbours"
    (dead? [1 1] (reproduce #{[0 0] })))
  (it "create no cell for four neighbours"
    (dead? [1 1] (reproduce #{[2 2] [0 0] [1 0] [0 1]}))))

(describe "rules should be applied simultaniously by "
  (it "creating cells from cells about to die"
    (alive? [1 1] (tick #{[1 0] [0 1] [1 2]})))
  (it "creating two cells from cells about to die"
    (= #{[1 1] [1 2]} (tick #{[0 2] [2 1] [2 2]}))))
