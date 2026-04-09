package com.controlzero.api.common.exception

open class BadRequestException(message: String) : RuntimeException(message)

class ConflictException(message: String) : RuntimeException(message)

class NotFoundException(message: String) : RuntimeException(message)

class UnauthorizedException(message: String) : RuntimeException(message)
