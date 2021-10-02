package isel.leic.daw.g29.ProjectIssuesManager.exceptions

abstract class BaseException(val name: String, val error: String):RuntimeException(name)



abstract class DbException(title: String, error: String) : BaseException(title, error)

class DbAccessException(error: String) : DbException("Problems with the DB Access", error)

class ControllerException(error: String) : DbException("Something went wrong with the Controller", error)

class DomainNotFoundException(error: String) : DbException("Domain not found Exception", error)

class KeyViolationException(error: String) : DbException("This key is already in use", error)

class ForeignKeyException(error: String) : DbException("Problems with foreign key(s)", error)

class NotNullException(error: String) : DbException("Fill all non-null fields with non-null values", error)

class IntegrityConstraintException(error: String) : DbException("Integrity Constraint Exception in database", error)

class CommentArchivedException(error: String) : DbException("Can't comment an archived issue", error)


class AuthenticationException(title: String, error: String) : BaseException(title, error)