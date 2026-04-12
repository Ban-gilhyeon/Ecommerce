package small.ecommerce.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import small.ecommerce.domain.product.ProductService
import small.ecommerce.domain.product.dto.ProductRequest
import small.ecommerce.domain.product.dto.ProductResponse

@RestController
@RequestMapping("/api/v1/products")
class ProductController(
    private val productService: ProductService
) {
    @PostMapping
    fun createProduct(
        @RequestBody request: ProductRequest.CreateProduct
    ): ResponseEntity<ProductResponse.CreateProduct> {
        return ResponseEntity.ok(ProductResponse.CreateProduct.from(productService.createProduct(request)))
    }

    @GetMapping
    fun readProducts(): ResponseEntity<List<ProductResponse.ReadProduct>> {
        return ResponseEntity.ok(productService.readProducts().map { ProductResponse.ReadProduct.from(it) })
    }

    @GetMapping("/{productId}")
    fun readProduct(
        @PathVariable productId: Long
    ): ResponseEntity<ProductResponse.ReadProduct> {
        return ResponseEntity.ok(ProductResponse.ReadProduct.from(productService.readProductById(productId)))
    }

    @GetMapping("/brand/{brandId}")
    fun readProductsByBrand(
        @PathVariable brandId: Long
    ): ResponseEntity<List<ProductResponse.ReadProduct>> {
        return ResponseEntity.ok(productService.readProductsByBrandId(brandId).map { ProductResponse.ReadProduct.from(it) })
    }

    @PutMapping("/{productId}")
    fun updateProduct(
        @PathVariable productId: Long,
        @RequestBody request: ProductRequest.UpdateProduct
    ): ResponseEntity<ProductResponse.UpdateProduct> {
        return ResponseEntity.ok(ProductResponse.UpdateProduct.from(productService.updateProduct(productId, request)))
    }

    @DeleteMapping("/{productId}")
    fun deleteProduct(
        @PathVariable productId: Long
    ): ResponseEntity<Unit> {
        productService.deleteProduct(productId)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/stocks")
    fun createProductStock(
        @RequestBody request: ProductRequest.CreateProductStock
    ): ResponseEntity<ProductResponse.CreateProductStock> {
        return ResponseEntity.ok(ProductResponse.CreateProductStock.from(productService.createProductStock(request)))
    }

    @GetMapping("/stocks")
    fun readProductStocks(): ResponseEntity<List<ProductResponse.ReadProductStock>> {
        return ResponseEntity.ok(productService.readProductStocks().map { ProductResponse.ReadProductStock.from(it) })
    }

    @GetMapping("/stocks/{stockId}")
    fun readProductStock(
        @PathVariable stockId: Long
    ): ResponseEntity<ProductResponse.ReadProductStock> {
        return ResponseEntity.ok(ProductResponse.ReadProductStock.from(productService.readProductStockById(stockId)))
    }

    @GetMapping("/{productId}/stocks")
    fun readProductStocksByProduct(
        @PathVariable productId: Long
    ): ResponseEntity<List<ProductResponse.ReadProductStock>> {
        return ResponseEntity.ok(productService.readProductStocksByProductId(productId).map { ProductResponse.ReadProductStock.from(it) })
    }

    @PutMapping("/stocks/{stockId}")
    fun updateProductStock(
        @PathVariable stockId: Long,
        @RequestBody request: ProductRequest.UpdateProductStock
    ): ResponseEntity<ProductResponse.UpdateProductStock> {
        return ResponseEntity.ok(ProductResponse.UpdateProductStock.from(productService.updateProductStock(stockId, request)))
    }

    @DeleteMapping("/stocks/{stockId}")
    fun deleteProductStock(
        @PathVariable stockId: Long
    ): ResponseEntity<Unit> {
        productService.deleteProductStock(stockId)
        return ResponseEntity.noContent().build()
    }
}
