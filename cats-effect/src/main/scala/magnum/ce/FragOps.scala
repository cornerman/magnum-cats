package com.augustnagro.magnum.ce

import cats.effect.Sync
import com.augustnagro.magnum.{DbCodec, DbCon, Frag}

extension(frag: Frag) {
  def queryF[F[_]: Sync, E](using DbCodec[E]): DbCon ?=> F[Vector[E]] = Sync[F].blocking(frag.query[E].run())
  def updateF[F[_]: Sync]: DbCon ?=> F[Int] = Sync[F].blocking(frag.update.run())
  def returningF[F[_]: Sync, E](using DbCodec[E]): DbCon ?=> F[Vector[E]] = Sync[F].blocking(frag.returning[E].run())
}
