package co.limoges.hq

import org.scalatra._
import org.scalatra.Ok
import scala.collection._
import scalate.ScalateSupport

class BrokerServlet extends ScalatraServlet {

	// Use our implementation of a broker to power the servlet
	var broker = new Broker;

	before() {
		contentType = "application/json"
	}
	
	get("/") {
		broker.list()
	}

	post("/:topic") {
		broker.add(params("topic"), params("event"))
	}
}

case class Transaction(
	topic:String,
	event:String
)

class Broker {
	var topics = mutable.Map[String, List[String]]().withDefaultValue(Nil)

	def list() = topics.keys

	def add(topic:String, event:String) : Transaction = {
		topics(topic) ::= event
		val tr = new Transaction(topic, event);
		tr
	}
}

