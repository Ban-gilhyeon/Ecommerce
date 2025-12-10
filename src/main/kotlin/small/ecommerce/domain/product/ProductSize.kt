package small.ecommerce.domain.product

enum class ProductSize {
    XS,
    S,
    M,
    L,
    XL,
    XXL;
    companion object{
        fun from(value: String): ProductSize =
            entries.firstOrNull{ it.name.equals(value, ignoreCase = true)}
                ?: throw IllegalArgumentException("Invalid Product Size: $value")
    }
}