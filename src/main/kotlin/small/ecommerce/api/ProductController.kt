package small.ecommerce.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = "Product", description = "Product API")
@SecurityRequirement(name = "bearerAuth")
class ProductController(
    private val productService: ProductService
) {
    @PostMapping
    @Operation(summary = "Create product")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Product created"),
            ApiResponse(responseCode = "400", description = "Invalid request"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
        ],
    )
    fun createProduct(
        @RequestBody request: ProductRequest.CreateProduct
    ): ResponseEntity<ProductResponse.CreateProduct> {
        return ResponseEntity.ok(ProductResponse.CreateProduct.from(productService.createProduct(request)))
    }

    @GetMapping
    @Operation(summary = "List products")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Products returned"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
        ],
    )
    fun readProducts(): ResponseEntity<List<ProductResponse.ReadProduct>> {
        return ResponseEntity.ok(productService.readProducts().map { ProductResponse.ReadProduct.from(it) })
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get product")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Product returned"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
            ApiResponse(responseCode = "404", description = "Product not found"),
        ],
    )
    fun readProduct(
        @Parameter(description = "Product ID")
        @PathVariable productId: Long
    ): ResponseEntity<ProductResponse.ReadProduct> {
        return ResponseEntity.ok(ProductResponse.ReadProduct.from(productService.readProductById(productId)))
    }

    @GetMapping("/brand/{brandId}")
    @Operation(summary = "List products by brand")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Products returned"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
            ApiResponse(responseCode = "404", description = "Brand not found"),
        ],
    )
    fun readProductsByBrand(
        @Parameter(description = "Brand ID")
        @PathVariable brandId: Long
    ): ResponseEntity<List<ProductResponse.ReadProduct>> {
        return ResponseEntity.ok(productService.readProductsByBrandId(brandId).map { ProductResponse.ReadProduct.from(it) })
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Update product")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Product updated"),
            ApiResponse(responseCode = "400", description = "Invalid request"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
            ApiResponse(responseCode = "404", description = "Product not found"),
        ],
    )
    fun updateProduct(
        @Parameter(description = "Product ID")
        @PathVariable productId: Long,
        @RequestBody request: ProductRequest.UpdateProduct
    ): ResponseEntity<ProductResponse.UpdateProduct> {
        return ResponseEntity.ok(ProductResponse.UpdateProduct.from(productService.updateProduct(productId, request)))
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Delete product")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Product deleted"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
            ApiResponse(responseCode = "404", description = "Product not found"),
        ],
    )
    fun deleteProduct(
        @Parameter(description = "Product ID")
        @PathVariable productId: Long
    ): ResponseEntity<Unit> {
        productService.deleteProduct(productId)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/stocks")
    @Operation(summary = "Create product stock")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Product stock created"),
            ApiResponse(responseCode = "400", description = "Invalid request"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
        ],
    )
    fun createProductStock(
        @RequestBody request: ProductRequest.CreateProductStock
    ): ResponseEntity<ProductResponse.CreateProductStock> {
        return ResponseEntity.ok(ProductResponse.CreateProductStock.from(productService.createProductStock(request)))
    }

    @GetMapping("/stocks")
    @Operation(summary = "List product stocks")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Product stocks returned"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
        ],
    )
    fun readProductStocks(): ResponseEntity<List<ProductResponse.ReadProductStock>> {
        return ResponseEntity.ok(productService.readProductStocks().map { ProductResponse.ReadProductStock.from(it) })
    }

    @GetMapping("/stocks/{stockId}")
    @Operation(summary = "Get product stock")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Product stock returned"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
            ApiResponse(responseCode = "404", description = "Product stock not found"),
        ],
    )
    fun readProductStock(
        @Parameter(description = "Product stock ID")
        @PathVariable stockId: Long
    ): ResponseEntity<ProductResponse.ReadProductStock> {
        return ResponseEntity.ok(ProductResponse.ReadProductStock.from(productService.readProductStockById(stockId)))
    }

    @GetMapping("/{productId}/stocks")
    @Operation(summary = "List stocks by product")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Product stocks returned"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
            ApiResponse(responseCode = "404", description = "Product not found"),
        ],
    )
    fun readProductStocksByProduct(
        @Parameter(description = "Product ID")
        @PathVariable productId: Long
    ): ResponseEntity<List<ProductResponse.ReadProductStock>> {
        return ResponseEntity.ok(productService.readProductStocksByProductId(productId).map { ProductResponse.ReadProductStock.from(it) })
    }

    @PutMapping("/stocks/{stockId}")
    @Operation(summary = "Update product stock")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Product stock updated"),
            ApiResponse(responseCode = "400", description = "Invalid request"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
            ApiResponse(responseCode = "404", description = "Product stock not found"),
        ],
    )
    fun updateProductStock(
        @Parameter(description = "Product stock ID")
        @PathVariable stockId: Long,
        @RequestBody request: ProductRequest.UpdateProductStock
    ): ResponseEntity<ProductResponse.UpdateProductStock> {
        return ResponseEntity.ok(ProductResponse.UpdateProductStock.from(productService.updateProductStock(stockId, request)))
    }

    @DeleteMapping("/stocks/{stockId}")
    @Operation(summary = "Delete product stock")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Product stock deleted"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
            ApiResponse(responseCode = "404", description = "Product stock not found"),
        ],
    )
    fun deleteProductStock(
        @Parameter(description = "Product stock ID")
        @PathVariable stockId: Long
    ): ResponseEntity<Unit> {
        productService.deleteProductStock(stockId)
        return ResponseEntity.noContent().build()
    }
}
