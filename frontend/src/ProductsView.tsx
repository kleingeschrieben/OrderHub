import {type ProductResponse, ProductsService} from "./generated-api";
import {useEffect, useState} from "react";
import ProductModal from "./ProductModal";

function ProductsView() {

    const [products, setProducts] = useState<ProductResponse[]>([]);
    const [showProductModal, setShowProductModal] = useState(false);
    const [selectedProduct, setSelectedProduct] = useState<ProductResponse | null>(null);

    useEffect(() => {
        async function loadProducts() {
            const result = await ProductsService.findAllProducts(0, 20);
            setProducts(result);
        }

        loadProducts();
    }, [])

    async function onDelete(product: ProductResponse) {
        const confirmed = confirm(`Are you sure you want to delete this product: "${product.name}"?`);
        if (confirmed) {
            await ProductsService.deleteProduct(product.id!);
            const updatedProducts = products.filter((p) => p.id !== product.id);
            setProducts(updatedProducts);
        }
    }

    async function onAddToCart(product: ProductResponse) {
        alert("ADD TO CART " + product.id!);
    }

    async function onEdit(product: ProductResponse) {
        setSelectedProduct(product);
        setShowProductModal(true);
    }

    function onAddProduct() {
        setSelectedProduct(null);
        setShowProductModal(true);
    }

    function onCloseProductModal() {
        setShowProductModal(false);
    }

    async function onProductSaved(saved: ProductResponse) {
        if (saved.id == null) {
            setShowProductModal(false);
            setSelectedProduct(null);
            return;
        }

        const exists = products.some((p) => p.id === saved.id);

        let updatedProducts: ProductResponse[];
        if (exists) {
            updatedProducts = products.map((p) => p.id === saved.id ? saved : p);
        } else {
            updatedProducts = [...products, saved];
        }

        setProducts(updatedProducts);
        setShowProductModal(false);
        setSelectedProduct(null);
    }


    return (
        <div>
            <div className="row mb-3">
                <div className="col">
                    <h2 className="mb-0">Products</h2>
                </div>
                <div className="col text-end">
                    <button type="button" className="btn btn-primary" onClick={onAddProduct}>
                        Add product
                    </button>
                </div>
            </div>
            <table className="table">
                <thead>
                <tr>
                    <th scope="col">ID</th>
                    <th scope="col">Name</th>
                    <th scope="col">Price</th>
                    <th scope="col">Stock</th>
                    <th scope="col">Action</th>
                </tr>
                </thead>
                <tbody>
                {products.map((product) => (
                    <tr key={product.id}>
                        <th scope="row">{product.id}</th>
                        <td>{product.name}</td>
                        <td>{product.price}</td>
                        <td>{product.stock}</td>
                        <td>
                            <div aria-label="Product actions">
                                <button type="button" className="btn me-1 btn-outline-dark" onClick={() => onAddToCart(product)}>🛒</button>
                                <button type="button" className="btn me-1 btn-outline-dark" onClick={() => onEdit(product)}>✏️</button>
                                <button type="button" className="btn btn-outline-dark" onClick={() => onDelete(product!)}>🗑️</button>
                            </div>
                        </td>

                    </tr>
                ))}
                </tbody>
            </table>
            <ProductModal open={showProductModal} onClose={onCloseProductModal} product={selectedProduct} onSaved={onProductSaved} />
        </div>
    );
}

export default ProductsView;
