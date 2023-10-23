/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.ptafrontend.views

import play.api.i18n.{Messages, MessagesApi}
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.ptafrontend.{JsoupHelpers, RequestHelpers, SpecBase}
import uk.gov.hmrc.ptafrontend.views.html.PtaHead

class PtaHeadSpec extends SpecBase with JsoupHelpers {

  "PtaHead" should {
    "include the pta css" in new Fixture {
      val links = content.select("link")

      links                      should have size 1
      links.first.attr("href") shouldBe "/foo-service/pta-frontend/assets/pta.css"
    }

    "include a nonce" in new Fixture {
      val links = content.select("link")
      links.first.attr("nonce") should be("a-nonce")
    }

    "include the correct media attribute" in new Fixture {
      val links = content.select("link")
      links.first.attr("media") should be("all")
    }

    "include the correct rel attribute" in new Fixture {
      val links = content.select("link")
      links.first.attr("rel") should be("stylesheet")
    }

    "include the correct type attribute" in new Fixture {
      val links = content.select("link")
      links.first.attr("type") should be("text/css")
    }
  }

  trait Fixture extends RequestHelpers {
    implicit val fakeRequest: FakeRequest[AnyContentAsEmpty.type] = getRequestWithNonce
    val messagesApi: MessagesApi                                  = app.injector.instanceOf[MessagesApi]
    implicit val messages: Messages                               = messagesApi.preferred(fakeRequest)
    val ptaHead: PtaHead                                          = app.injector.instanceOf[PtaHead]
    ptafrontend.RoutesPrefix.setPrefix("/foo-service/pta-frontend")
    val content: HtmlFormat.Appendable                            = ptaHead()
  }
}
