package me.spencernold.kwaf.database

import org.bson.Document
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.function.Consumer


abstract class MongoDatabase(private val executor: ExecutorService = Executors.newSingleThreadExecutor()): Database() {

    override fun open() {
        getDriver(MongoDriver::class.java).init()
    }

    fun insert(db: String, collection: String, statement: String) {
        val driver  = getDriver(MongoDriver::class.java)
        val database = driver.getDatabase(db)
        val connection = driver.getCollection(database!!, collection)
        connection.insertOne(Document.parse(statement))
    }

    fun insertAsync(db: String, collection: String, statement: String): CompletableFuture<Void> {
        return CompletableFuture.runAsync( { insert(db, collection, statement) }, executor)
    }

    fun queryAll(db: String, collection: String): List<String> {
        val driver = getDriver(MongoDriver::class.java)
        val database = driver.getDatabase(db)
        val connection = driver.getCollection(database!!, collection)
        val iterator = connection.find().iterator()
        val data = mutableListOf<String>()
        while (iterator.hasNext())
            data.add(iterator.next().toJson())
        return data
    }

    fun query(db: String, collection: String, statement: String): String? {
        val driver = getDriver(MongoDriver::class.java)
        val database = driver.getDatabase(db)
        val connection = driver.getCollection(database!!, collection)
        return connection.find(Document.parse(statement)).first()?.toJson()
    }

    fun queryAsync(db: String, collection: String, statement: String): CompletableFuture<String> {
        return CompletableFuture.supplyAsync( { query(db, collection, statement) }, executor)
    }

    fun delete(db: String, collection: String, statement: String) {
        val driver = getDriver(MongoDriver::class.java)
        val database = driver.getDatabase(db)
        val connection = driver.getCollection(database!!, collection)
        connection.deleteOne(Document.parse(statement))
    }
}