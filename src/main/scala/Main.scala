import cats.data.Kleisli
import cats.effect._
import cats.implicits.catsSyntaxApplicativeId
import com.comcast.ip4s.IpLiteralSyntax
import org.http4s._
import org.http4s.server._
import org.http4s.implicits._
import org.http4s.dsl.io._
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.headers.Authorization

case class User(id: Long, name: String)
object Main  extends IOApp.Simple
{

  val routes: HttpRoutes[IO] = HttpRoutes.of {
    case GET -> Root / "welcome" / user => Ok(s"Welcome, $user")

  }

  val server = EmberServerBuilder
    .default[IO]
    .withHost(ipv4"0.0.0.0")
    .withPort(port"8080")
    .withHttpApp(routes.orNotFound)
    .build

  //simple auth method - basic
  // Request[IO]  => IO[Either[String, User]
  // Kleisli[IO, Request[IO], Either [String,User]]
  // Kleisli[F,A,B] equivalent to A => F[B]
   val basicAuthMethod = Kleisli.apply[IO, Request[IO], Either[String,User]] {
    req =>
      // auth logic
      val authHeader = req.headers.get[Authorization]
      authHeader match {
        case Some(Authorization(BasicCredentials(creds))) => IO(Right(User(1L /*fetch from db */, creds._1)))
        case Some(_) => IO(Left("No basic credentials"))
        case None => IO(Left("Unauthorized!!"))
      }
  }

  val onFailure: AuthedRoutes[String, IO] = Kleisli { (req: AuthedRequest[IO, String]) =>
  Option.pure[IO](Response[IO](status = Status.Unauthorized))
}

  // middleware
  val userBasicAuthMiddleware: AuthMiddleware[IO, User] = AuthMiddleware(basicAuthMethod,  onFailure)
  override def run = server.use(_ => IO.never).void


}