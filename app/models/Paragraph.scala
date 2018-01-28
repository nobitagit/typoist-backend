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
      val stockAsList: Seq[(String, JsString)] = Seq(
        "text" -> JsString(p.text)
      )
      JsObject(stockAsList)
    }
  }
}

// https://stackoverflow.com/a/24459770/1446845
case class ParagraphSeq(text: Seq[String])

object ParagraphSeq {

  implicit object ParagraphSeqFormat extends Format[ParagraphSeq] {
    // convert from JSON string to a Stock object (de-serializing from JSON)
    def reads(json: JsValue): JsResult[ParagraphSeq] = {
      val text: Seq[String] = (json \ "text").as[Seq[String]]
      JsSuccess(ParagraphSeq(text))
    }

    // convert from Stock object to JSON (serializing to JSON)
    def writes(p: ParagraphSeq): JsValue = {
      // JsObject requires Seq[(String, play.api.libs.json.JsValue)]
      val ret: Seq[(String, JsArray)] = Seq(
        "text" -> JsArray( p.text.map(JsString) toIndexedSeq )
      )
      JsObject(ret)
    }
  }
}