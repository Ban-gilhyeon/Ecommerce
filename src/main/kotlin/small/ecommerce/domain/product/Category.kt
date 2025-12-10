package small.ecommerce.domain.product

enum class Category {
    OUTER,
    SHIRTS,
    T_SHIRTS,
    SWEAT_AND_KNIT,
    PANTS,
    HALF_PANTS,
    HEAD_WEAR,
    ACCESSORY,
    BAG,
    SHOE,
    UNDER_WEAR;

    companion object{
        fun from(value: String): Category =
            entries.firstOrNull{ it.name.equals(value, ignoreCase = true)}
                ?: throw IllegalArgumentException("Invalid category: $value")
    }


}