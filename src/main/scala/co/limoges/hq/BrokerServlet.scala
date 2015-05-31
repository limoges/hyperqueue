package co.limoges.hq

import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json._
import scala.collection._
import scalate.ScalateSupport

class BrokerServlet extends ScalatraServlet with ScalateSupport with JacksonJsonSupport {

	// Use our implementation of a broker to power the servlet
	var broker = new Broker;

	// Use automatic JSON serialization of case classes
	protected implicit val jsonFormats: Formats = DefaultFormats

	// The service deals entirely in the JSON format, contentType
	// should therefore reflect that.
	before() {
		contentType = formats("json")
	}
	
	get("/") {
		broker.list()
	}

	post("/:topic") {
		broker.add(params("topic"), params("event"))
	}
}

case class Transaction(topic:String, event:String)

class Broker {
	var topics = mutable.Map[String, List[String]]().withDefaultValue(Nil)

	def list() = topics.keys

	def add(topic:String, event:String) : Transaction = {
		topics(topic) ::= event
		new Transaction(topic, event);
	}
}

