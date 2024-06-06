package com.augustnagro.magnum.ce

import cats.effect.Sync
import com.augustnagro.magnum.{DbCodec, DbCon, Frag}

class DbTask[F[_], R](val run: DbCon ?=> F[R])

extension(frag: Frag) {
  def queryF[F[_]: Sync, E](using DbCodec[E]): DbTask[F, Vector[E]] = DbTask(Sync[F].blocking(frag.query[E].run()))
  def updateF[F[_]: Sync]: DbTask[F, Int] = DbTask(Sync[F].blocking(frag.update.run()))
  def returningF[F[_]: Sync, E](using DbCodec[E]): DbTask[F, Vector[E]] = DbTask(Sync[F].blocking(frag.returning[E].run()))
}
