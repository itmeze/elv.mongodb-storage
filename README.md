# elv and mongodb storage

Elv is a Clojure library designed to help with logging and viewing exceptions on the ring server.

Efv.mongodb-storage stores elv logs to mongodb document database

## Usage

## Geting started

Elv.mongodb-storage artifacts are [deployed to clojars] (https://clojars.org/elv.mongodb-storage) 

With Leiningen:

    [elv.mongodb-storage "0.1.0"]

With Gradle:
    compile "elv.mongodb-storage:elv.mongodb-storage:0.1.0"

With Maven:

    <dependency>
      <groupId>elv.mongodb-storage</groupId>
      <artifactId>elv.mongodb-storage</artifactId>
      <version>0.1.1</version>
    </dependency>
    

##Usage

``` clojure
(require '[elv.core :refer :all])

(require '[elv.mongodb-storage :refer :all])

(def app
  (-> your-handler (wrap-exception :storage (->MongoStorage {:uri     "mongodb://user:password@ds029911.mongolab.com:29911/elv-test" :coll "elv-test"}))))
```

MongoStorage is based on clojure's monger library and expects a map as a parameter:
- :uri - this is the uri (commonly used by PaaS)
- :conn - Monger's collection (not required if :uri passed)
- :coll - name of the collection where to store elv's logs.

## License

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

