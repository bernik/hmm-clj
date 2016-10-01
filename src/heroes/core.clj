(ns heroes.core
  (:require [clocop.core :refer :all]
            [clocop.constraints :refer :all]))

(def titan    {:gold 5000 :diamonds 3 :mercury 1 :strength 300 :available 10})
(def naga     {:gold 1000 :diamonds 2 :mercury 0 :strength 120 :available 20})
(def gin      {:gold 750  :diamonds 1 :mercury 1 :strength 60  :available 30})
(def mag      {:gold 500  :diamonds 1 :mercury 1 :strength 40  :available 55})
(def golem    {:gold 400  :diamonds 0 :mercury 1 :strength 35  :available 60})
(def gorgoyle {:gold 200  :diamonds 0 :mercury 0 :strength 20  :available 110})
(def gremlin  {:gold 70   :diamonds 0 :mercury 0 :strength 4   :available 500})

(def all-creatures [titan naga gin mag golem gorgoyle gremlin])
(def flying-creatures [gin gorgoyle])
(def range-creatures [titan mag gremlin])

(def denis-gold 200000)
(def denis-diamonds 115)
(def denis-mercury 80)

(def max-allowed-gold (+ denis-gold (* denis-diamonds 500)))

(defn -main [& args]
  (clojure.pprint/pprint
   (with-store (store)
     (let [strength       (int-var "strength"  0 (->> all-creatures
                                                      (map #(* (:strength %) (:available %)))
                                                      (apply +)))
           titans         (int-var "titans"    0 (:available titan))
           nagas          (int-var "nagas"     0 (:available naga))
           gins           (int-var "gins"      0 (:available gin))
           mags           (int-var "mags"      0 (:available mag))
           golems         (int-var "golems"    0 (:available golem))
           gorgoyles      (int-var "gorgoyles" 0 (:available gorgoyle))
           gremlins       (int-var "gremlins"  0 (:available gremlin))
           gold           (int-var "gold"      0 max-allowed-gold)
           diamonds       (int-var "diamonds"  0 denis-diamonds)
           mercury        (int-var "mercury"   0 denis-mercury)
           denis-gold     (int-var denis-gold)
           denis-diamonds (int-var denis-diamonds)
           denis-mercury  (int-var denis-mercury)
           diamond-cost   (int-var 500)]
       ;; mercury
       (constrain! ($= mercury ($+ ($* titans (:mercury titan))
                                   ($* nagas (:mercury naga))
                                   ($* gins (:mercury gin))
                                   ($* mags (:mercury mag))
                                   ($* golems (:mercury golem))
                                   ($* gorgoyles (:mercury gorgoyle))
                                   ($* gremlins (:mercury gremlin)))))
       (constrain! ($<= mercury denis-mercury))
       ;; diamonds
       (constrain! ($= diamonds ($+ ($* titans (:diamonds titan))
                                    ($* nagas (:diamonds naga))
                                    ($* gins (:diamonds gin))
                                    ($* mags (:diamonds mag))
                                    ($* golems (:diamonds golem))
                                    ($* gorgoyles (:diamonds gorgoyle))
                                    ($* gremlins (:diamonds gremlin)))))
       (constrain! ($<= diamonds denis-diamonds))
       ;; gold
       (constrain! ($= gold ($+ ($* titans (:gold titan))
                                ($* nagas (:gold naga))
                                ($* gins (:gold gin))
                                ($* mags (:gold mag))
                                ($* golems (:gold golem))
                                ($* gorgoyles (:gold gorgoyle))
                                ($* gremlins (:gold gremlin)))))
       (constrain! ($<= gold ($+ denis-gold
                                 ($* diamond-cost
                                     ($- denis-diamonds
                                         diamonds)))))
       ;; strength
       (constrain! ($>= ($+ ($* titans (:strength titan))
                            ($* mags (:strength mag))
                            ($* gremlins (:strength gremlin)))
                        4000))
       (constrain! ($>= ($+ ($* gins (:strength gin))
                            ($* gorgoyles (:strength gorgoyle)))
                        2000))
       (constrain! ($= strength ($+ ($* titans (:strength titan))
                                    ($* nagas (:strength naga))
                                    ($* gins (:strength gin))
                                    ($* mags (:strength mag))
                                    ($* golems (:strength golem))
                                    ($* gorgoyles (:strength gorgoyle))
                                    ($* gremlins (:strength gremlin)))))
       (solve! :pick-val :max)))))
