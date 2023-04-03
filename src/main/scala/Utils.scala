import requests.Response

object Utils {

  def getData(siteName: String, route: String): String = {
    val r = requests.get(
      siteName + route,
      check = false,
      readTimeout = 200000,
      connectTimeout = 200000,
      verifySslCerts = false
    )
    println(r.text().replace("\\n", ""))
    r.text().replace("\\n", "")
  }

  def sendData(data: String, siteName: String, route: String): Response = {
    val r = requests.post(
      siteName + route,
      check = false,
      verifySslCerts = false,
      readTimeout = 200000,
      connectTimeout = 200000,
      headers = Map("accept" -> "text/plain", "Content-Type" -> "application/json-patch+json"),
      data = data)
    r
  }

}
