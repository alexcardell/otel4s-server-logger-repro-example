package example

import cats.Monad
import cats.Show
import cats.implicits._
// import natchez.Kernel
// import natchez.Trace
// import natchez.TraceValue
// import natchez.TraceValue.BooleanValue
// import natchez.TraceValue.NumberValue
// import natchez.TraceValue.StringValue
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.otel4s.trace.SpanContext
import org.typelevel.otel4s.trace.Tracer

trait TracedLogger[F[_]] extends SelfAwareStructuredLogger[F]

object TracedLogger {

  // def apply[F[_]: Monad: Tracer](
  //     log: SelfAwareStructuredLogger[F]
  // ): TracedLogger[F] = apply[F](log, lowerCase(_))

  def apply[F[_]: Monad: Tracer](
      log: SelfAwareStructuredLogger[F]
      // kernelCtx: Kernel => Map[String, String]
  ): TracedLogger[F] = {
    new TracedLogger[F] {
      val traceLog = Tracer[F].currentSpanContext
        .map {
          case Some(context) =>
            log.addContext(
              Map(
                "trace_id" -> context.traceIdHex,
                "span_id" -> context.spanIdHex
              )
            )
          case None => log
        }

      def error(message: => String): F[Unit] = traceLog >>= (_.error(message))

      def warn(message: => String): F[Unit] = traceLog >>= (_.warn(message))

      def info(message: => String): F[Unit] = traceLog >>= (_.info(message))

      def debug(message: => String): F[Unit] = traceLog >>= (_.debug(message))

      def trace(message: => String): F[Unit] = traceLog >>= (_.trace(message))

      def error(t: Throwable)(message: => String): F[Unit] =
        traceLog >>= (_.error(t)(message))

      def warn(t: Throwable)(message: => String): F[Unit] =
        traceLog >>= (_.warn(t)(message))

      def info(t: Throwable)(message: => String): F[Unit] =
        traceLog >>= (_.info(t)(message))

      def debug(t: Throwable)(message: => String): F[Unit] =
        traceLog >>= (_.debug(t)(message))

      def trace(t: Throwable)(message: => String): F[Unit] =
        traceLog >>= (_.trace(t)(message))

      def trace(ctx: Map[String, String])(msg: => String): F[Unit] =
        traceLog >>= (_.trace(ctx)(msg))

      def trace(ctx: Map[String, String], t: Throwable)(
          msg: => String
      ): F[Unit] = traceLog >>= (_.trace(ctx, t)(msg))

      def debug(ctx: Map[String, String])(msg: => String): F[Unit] =
        traceLog >>= (_.debug(ctx)(msg))

      def debug(ctx: Map[String, String], t: Throwable)(
          msg: => String
      ): F[Unit] = traceLog >>= (_.debug(ctx, t)(msg))

      def info(ctx: Map[String, String])(msg: => String): F[Unit] =
        traceLog >>= (_.info(ctx)(msg))

      def info(ctx: Map[String, String], t: Throwable)(
          msg: => String
      ): F[Unit] = traceLog >>= (_.info(ctx, t)(msg))

      def warn(ctx: Map[String, String])(msg: => String): F[Unit] =
        traceLog >>= (_.warn(ctx)(msg))

      def warn(ctx: Map[String, String], t: Throwable)(
          msg: => String
      ): F[Unit] = traceLog >>= (_.warn(ctx, t)(msg))

      def error(ctx: Map[String, String])(msg: => String): F[Unit] =
        traceLog >>= (_.error(ctx)(msg))

      def error(ctx: Map[String, String], t: Throwable)(
          msg: => String
      ): F[Unit] = traceLog >>= (_.error(ctx, t)(msg))

      def isTraceEnabled: F[Boolean] = log.isTraceEnabled

      def isDebugEnabled: F[Boolean] = log.isDebugEnabled

      def isInfoEnabled: F[Boolean] = log.isInfoEnabled

      def isWarnEnabled: F[Boolean] = log.isWarnEnabled

      def isErrorEnabled: F[Boolean] = log.isErrorEnabled

    }
  }

}
