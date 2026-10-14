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
import small.ecommerce.common.response.ApiResponse as CommonApiResponse
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
    ): ResponseEntity<CommonApiResponse<ProductResponse.CreateProduct>> {
        return ResponseEntity.ok(CommonApiResponse.success(ProductResponse.CreateProduct.from(productService.createProduct(request))))
    }

    @GetMapping
    @Operation(summary = "List products")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Products returned"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
        ],
    )
    fun readProducts(): ResponseEntity<CommonApiResponse<ProductResponse.ReadProductList>> {
        return ResponseEntity.ok(CommonApiResponse.success(ProductResponse.ReadProductList.from(productService.readProducts())))
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
    ): ResponseEntity<CommonApiResponse<ProductResponse.ReadProduct>> {
        return ResponseEntity.ok(CommonApiResponse.success(ProductResponse.ReadProduct.from(productService.readProductById(productId))))
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
    ): ResponseEntity<CommonApiResponse<ProductResponse.ReadProductList>> {
        return ResponseEntity.ok(CommonApiResponse.success(ProductResponse.ReadProductList.from(productService.readProductsByBrandId(brandId))))
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
    ): ResponseEntity<CommonApiResponse<ProductResponse.UpdateProduct>> {
        return ResponseEntity.ok(CommonApiResponse.success(ProductResponse.UpdateProduct.from(productService.updateProduct(productId, request))))
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
    ): ResponseEntity<CommonApiResponse<ProductResponse.DeleteProduct>> {
        productService.deleteProduct(productId)
        return ResponseEntity.ok(CommonApiResponse.success(ProductResponse.DeleteProduct.from(productId)))
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
    ): ResponseEntity<CommonApiResponse<ProductResponse.CreateProductStock>> {
        return ResponseEntity.ok(CommonApiResponse.success(ProductResponse.CreateProductStock.from(productService.createProductStock(request))))
    }

    @GetMapping("/stocks")
    @Operation(summary = "List product stocks")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Product stocks returned"),
            ApiResponse(responseCode = "401", description = "Authentication required"),
        ],
    )
    fun readProductStocks(): ResponseEntity<CommonApiResponse<ProductResponse.ReadProductStockList>> {
        return ResponseEntity.ok(CommonApiResponse.success(ProductResponse.ReadProductStockList.from(productService.readProductStocks())))
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
    ): ResponseEntity<CommonApiResponse<ProductResponse.ReadProductStock>> {
        return ResponseEntity.ok(CommonApiResponse.success(ProductResponse.ReadProductStock.from(productService.readProductStockById(stockId))))
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
    ): ResponseEntity<CommonApiResponse<ProductResponse.ReadProductStockList>> {
        return ResponseEntity.ok(CommonApiResponse.success(ProductResponse.ReadProductStockList.from(productService.readProductStocksByProductId(productId))))
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
    ): ResponseEntity<CommonApiResponse<ProductResponse.UpdateProductStock>> {
        return ResponseEntity.ok(CommonApiResponse.success(ProductResponse.UpdateProductStock.from(productService.updateProductStock(stockId, request))))
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
    ): ResponseEntity<CommonApiResponse<ProductResponse.DeleteProductStock>> {
        productService.deleteProductStock(stockId)
        return ResponseEntity.ok(CommonApiResponse.success(ProductResponse.DeleteProductStock.from(stockId)))
    }
}
