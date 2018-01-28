package models

import play.api.libs.json._

case class Stock(symbol: String, price: Double)

object Stock {
  implicit object StockFormat extends Format[Stock] {
    // convert from JSON string to a Stock object (de-serializing from JSON)
    def reads(json: JsValue): JsResult[Stock] = {
      val symbol = (json \ "symbol").as[String]
      val price = (json \ "price").as[Double]
      JsSuccess(Stock(symbol, price))
    }

    // convert from Stock object to JSON (serializing to JSON)
    def writes(s: Stock): JsValue = {
      // JsObject requires Seq[(String, play.api.libs.json.JsValue)]
      val stockAsList = Seq("symbol" -> JsString(s.symbol),
        "price" -> JsNumber(s.price))
      JsObject(stockAsList)
    }
  }
}

case class Paragraph(text: String)

object Paragraph {

  implicit object ParagraphFormat extends Format[Paragraph] {
    // convert from JSON string to a Stock object (de-serializing from JSON)
    def reads(json: JsValue): JsResult[Paragraph] = {
      val text = (json \ "text").as[String]
      JsSuccess(Paragraph(text))
    }

    // convert from Stock object to JSON (serializing to JSON)
    def writes(p: Paragraph): JsValue = {
      // JsObject requires Seq[(String, play.api.libs.json.JsValue)]
      val stockAsList = Seq(
        "text" -> JsString(p.text)
      )
      JsObject(stockAsList)
    }
  }
}