(ns elv.mongodb-storage
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.query :as q]
            [elv.storage :refer :all])
  (:import org.bson.types.ObjectId))

(defn- with-mongo-connection 
	[opts fx]
	(if (:uri opts)
		(let [{:keys [conn db]} (mg/connect-via-uri (:uri opts))
			res (fx {:conn conn :db db :coll (:coll opts)})]
			(mg/disconnect conn)
			res)
		(fx opts)))

(defrecord MongoStorage [opts]
	LogStorage
	(store
		[this error]
		(with-mongo-connection opts
			(fn [cobj]
				(mc/insert (:db cobj) (:coll cobj) (dissoc (assoc error :body (slurp (:body error))) :id )))))
	(retrive
		[this id]
		(with-mongo-connection opts
			(fn [cobj]
				(mc/find-map-by-id (:db cobj) (:coll cobj) (ObjectId. id)))))
	(search
		[this page size conditions order]
		(if conditions (throw (Exception. "Filtering is currently not supported for local FileSystem Storage")))
		(if order (throw (Exception. "Ordering is currently not supported for local FileSystem Storage")))
		(with-mongo-connection opts (fn [cobj]
			(doall 
				(map #(dissoc (assoc % :id (str (:_id %))) :_id) 
					(q/with-collection (:db cobj) (:coll cobj)
						(q/find {})
						(q/paginate :page page :per-page size))))))))
