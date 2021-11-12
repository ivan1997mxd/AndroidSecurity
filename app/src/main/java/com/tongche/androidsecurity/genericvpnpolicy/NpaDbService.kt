package com.tongche.androidsecurity.genericvpnpolicy

import android.util.Log
import com.google.gson.Gson
import com.mongodb.ConnectionString
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import com.tongche.androidsecurity.model.Report
import com.tongche.androidsecurity.model.Result
import com.tongche.androidsecurity.model.State
import org.bson.Document
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.json.JSONArray
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class NpaDbService(private val connectionString: Supplier<ConnectionString>) {
    companion object {
        private const val DB_NAME = "npa_db"
        private const val COLLECTION_NAME = "Report"
        private fun mapToDocuments(npaDataList: List<String>): List<Document> {
            val documentList = ArrayList<Document>()
            for (json in npaDataList) {
                documentList.add(Document.parse(json))
            }
            return documentList
        }
    }

    fun save(npaData: List<String>) {
        try {
            database.getCollection(COLLECTION_NAME).insertMany(mapToDocuments(npaData))
        } catch (e: Throwable) {
            Log.e(NpaDbService::class.java.name, e.message!!)
        }
    }
    @Suppress("UNCHECKED_CAST")
    fun read(): ArrayList<Report> {
        var reports = ArrayList<Report>()
        try {
            val cursor = database.getCollection(COLLECTION_NAME).find()
            val it = cursor.iterator()
            while (it.hasNext()) {
                val report_json = it.next().toJson().toString()
                var report_map: Map<String, Any> = HashMap()
                report_map = Gson().fromJson(report_json, report_map.javaClass)
                val state_map = report_map.get("state") as Map<String, String>
                val harmless = state_map.get("harmless")
                val type_unsupported = state_map.get("type-unsupported")
                val suspicious = state_map.get("suspicious")
                val confirmed = state_map.get("confirmed-timeout")
                val timeout = state_map.get("timeout")
                val failure = state_map.get("failure")
                val malicious = state_map.get("malicious")
                val undetected = state_map.get("undetected")
                val result_map = report_map.get("results") as Map<String, Map<String, String>>
                val state = State(
                    harmless = harmless!!,
                    type_unsupported = type_unsupported!!,
                    suspicious = suspicious!!,
                    confirmed_timeout = confirmed!!,
                    timeout = timeout!!,
                    failure = failure!!,
                    malicious = malicious!!,
                    undetected = undetected!!
                )
                var results = ArrayList<Result>()
                for ((k,v) in result_map){
                    val result = Result(
                        category = v.get("category").toString(),
                        engine_name = v.get("engine_name").toString(),
                        method = v.get("method").toString(),
                        result = v.get("result").toString()
                    )
                    results.add(result)
                }
                val report = Report(
                    name = report_map.get("target").toString(),
                    score = report_map.get("score").toString(),
                    riskRank = report_map.get("riskRank").toString(),
                    state = state, details = results)
                System.out.println(report)
                reports.add(report)
            }
        } catch (e: Throwable) {
            Log.e(NpaDbService::class.java.name, e.message!!)
        }
        return reports
    }

    fun testDbConnection(): Boolean {
        try {
            val database = database
            val num = database.getCollection("foo").countDocuments()
        } catch (e: Throwable) {
            Log.e(NpaDbService::class.java.name, e.message!!)
            return false
        }
        return true
    }

    private val database: MongoDatabase
        get() = MongoClients.create(connectionString.get()).getDatabase(DB_NAME)


}
