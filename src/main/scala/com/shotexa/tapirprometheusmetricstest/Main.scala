package com.shotexa
package tapirprometheusmetricstest

import org.http4s.HttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import sttp.model.StatusCode
import sttp.tapir.{PublicEndpoint, endpoint, statusCode}
import sttp.tapir.server.http4s.ztapir.ZHttp4sServerInterpreter
import sttp.tapir.ztapir.*
import zio.*
import zio.interop.catz.*


object Main extends ZIOAppDefault {
  override def run: ZIO[Any, Throwable, Unit] = {
    ZHttp4sBlazeServer.start(List(Endpoints.ping), 8080).debug.unit
  }
}


object ZHttp4sBlazeServer {

  def start(endpoints: List[ZServerEndpoint[Any, Any]], port: Int): Task[ExitCode] = {
    ZIO.attempt {
      val http4sEndpoints: HttpRoutes[Task] =
        ZHttp4sServerInterpreter()
          .from(endpoints)
          .toRoutes

      val serve: Task[Unit] =
        ZIO.executor.flatMap(executor =>
          BlazeServerBuilder[Task]
            .withExecutionContext(executor.asExecutionContext)
            .bindHttp(port, "0.0.0.0")
            .withHttpApp(Router("/" -> http4sEndpoints).orNotFound)
            .serve
            .compile
            .drain
        )

      serve.exitCode
    }.flatten

  }
}


object Endpoints {

  val ping: ZServerEndpoint[Any, Any] =
    endpoint.in("ping")
      .out(stringBody.description("ping"))
      .errorOut(stringBody.description("some err"))
      .tag("Ping").zServerLogic { (_: Unit) =>
      ZIO.succeed("OK")
    }

}


