package small.ecommerce.domain.user.dto

import small.ecommerce.domain.user.User

sealed class UserResponse {
    data class Create(
        val id: Long,
        val email: String,
        val name: String,
        val role: String,
        val address: String,
    ) : UserResponse() {
        companion object {
            fun from(user: User): Create =
                Create(
                    id = user.id,
                    email = user.email,
                    name = user.name,
                    role = user.role.name,
                    address = user.address,
                )
        }
    }

    data class Read(
        val id: Long,
        val email: String,
        val name: String,
        val role: String,
        val address: String,
    ) : UserResponse() {
        companion object {
            fun from(user: User): Read =
                Read(
                    id = user.id,
                    email = user.email,
                    name = user.name,
                    role = user.role.name,
                    address = user.address,
                )
        }
    }

    data class ReadList(
        val users: List<Read>,
    ) : UserResponse() {
        companion object {
            fun from(users: List<User>): ReadList =
                ReadList(users = users.map { Read.from(it) })
        }
    }

    data class Update(
        val id: Long,
        val email: String,
        val name: String,
        val role: String,
        val address: String,
    ) : UserResponse() {
        companion object {
            fun from(user: User): Update =
                Update(
                    id = user.id,
                    email = user.email,
                    name = user.name,
                    role = user.role.name,
                    address = user.address,
                )
        }
    }

    data class Delete(
        val id: Long,
    ) : UserResponse() {
        companion object {
            fun from(userId: Long): Delete =
                Delete(id = userId)
        }
    }
}
