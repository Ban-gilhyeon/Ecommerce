package small.ecommerce.domain.product

enum class Gender {
    MEN,
    WOMEN,
    UNISEX;

    companion object{
        fun from(value: String): Gender =
            entries.firstOrNull{ it.name.equals(value, ignoreCase = true)}
                ?: throw IllegalArgumentException("Invalid Gender: $value")
    }
}