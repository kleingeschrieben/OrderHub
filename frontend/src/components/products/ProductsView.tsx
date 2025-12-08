import { type ProductResponse, ProductsService } from "../../generated-api";
import { useEffect, useState, type ChangeEvent } from "react";
import ProductModal from "./ProductModal.tsx";
import PaginationBar from "../utils/PaginationBar.tsx";

type ProductsViewProps = {
    onAddToCart: (product: ProductResponse) => void;
    isAdmin: boolean;
};

function ProductsView({ onAddToCart, isAdmin }: ProductsViewProps) {
    const [products, setProducts] = useState<ProductResponse[]>([]);
    const [showProductModal, setShowProductModal] = useState(false);
    const [selectedProduct, setSelectedProduct] = useState<ProductResponse | null>(null);
    const [page, setPage] = useState(0);
    const [search, setSearch] = useState("");
    const pageSize = 10;

    useEffect(() => {
        let cancelled = false;
        const query = search.trim() || undefined;
        ProductsService.findAllProducts(page, pageSize, query).then(result => {
            if (!cancelled) {
                setProducts(result);
            }
        });
        return () => {
            cancelled = true;
        };
    }, [page, search]);

    function handleSearchChange(event: ChangeEvent<HTMLInputElement>) {
        setPage(0);
        setSearch(event.target.value);
    }

    async function onDelete(product: ProductResponse) {
        if (!isAdmin) return;
        const confirmed = confirm(`Are you sure you want to delete this product: "${product.name}"?`);
        if (confirmed) {
            await ProductsService.deleteProduct(product.id!);
            const updatedProducts = products.filter(p => p.id !== product.id);
            setProducts(updatedProducts);
        }
    }

    async function onEdit(product: ProductResponse) {
        if (!isAdmin) return;
        setSelectedProduct(product);
        setShowProductModal(true);
    }

    function onAddProduct() {
        if (!isAdmin) return;
        setSelectedProduct(null);
        setShowProductModal(true);
    }

    function onCloseProductModal() {
        setShowProductModal(false);
    }

    async function onProductSaved(saved: ProductResponse) {
        const exists = products.some(p => p.id === saved.id);
        let updatedProducts: ProductResponse[];
        if (exists) {
            updatedProducts = products.map(p => (p.id === saved.id ? saved : p));
        } else {
            updatedProducts = [...products, saved];
        }
        setProducts(updatedProducts);
        setShowProductModal(false);
        setSelectedProduct(null);
    }

    function goToPreviousPage() {
        if (page === 0) return;
        setPage(page - 1);
    }

    function goToNextPage() {
        if (products.length < pageSize) return;
        setPage(page + 1);
    }

    return (
        <div className="row justify-content-center">
            <div className="col-12 col-lg-10">
                <div className="d-flex justify-content-between align-items-center mb-3">
                    <h2 className="mb-0">Products</h2>
                    <div className="d-flex align-items-center gap-2">
                        <input type="text" className="form-control form-control-sm" placeholder="Search..." value={search} onChange={handleSearchChange}/>
                        {isAdmin && (<button type="button" className="btn btn-primary btn-sm form-control form-control-sm" onClick={onAddProduct}>Add Product</button>)}
                    </div>
                </div>
                <table className="table table-striped align-middle text-center">
                    <thead>
                    <tr>
                        <th scope="col">ID</th>
                        <th scope="col" className="text-start">Name</th>
                        <th scope="col">Price</th>
                        <th scope="col">Stock</th>
                        <th scope="col">Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    {products.map(product => (
                        <tr key={product.id}>
                            <th scope="row">{product.id}</th>
                            <td className="text-start">{product.name}</td>
                            <td>{product.price}</td>
                            <td>{product.stock}</td>
                            <td className="text-center">
                                <div aria-label="Product actions" className="btn-group" role="group">
                                    <button type="button" className="btn btn-outline-dark" onClick={() => onAddToCart(product)}>🛒</button>
                                    {isAdmin && (
                                        <>
                                            <button type="button" className="btn btn-outline-dark" onClick={() => onEdit(product)}>✏️</button>
                                            <button type="button" className="btn btn-outline-dark" onClick={() => onDelete(product)}>🗑️</button>
                                        </>
                                    )}
                                </div>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
                <PaginationBar
                    page={page}
                    canGoPrevious={page > 0}
                    canGoNext={products.length >= pageSize}
                    onPrevious={goToPreviousPage}
                    onNext={goToNextPage}
                />
                <ProductModal
                    open={showProductModal}
                    onClose={onCloseProductModal}
                    product={selectedProduct}
                    onSaved={onProductSaved}
                />
            </div>
        </div>
    );
}

export default ProductsView;
