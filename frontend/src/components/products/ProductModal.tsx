import {useEffect, useState, type FormEvent} from "react";
import {ProductsService, type ProductRequest, type ProductResponse} from "../../generated-api";

type ProductModalProps = {
    open: boolean;
    onClose: () => void;
    product: ProductResponse | null;
    onSaved: (product: ProductResponse) => void;
};

function ProductModal({ open, onClose, product, onSaved }: ProductModalProps) {
    const [name, setName] = useState("");
    const [price, setPrice] = useState("");
    const [stock, setStock] = useState("");
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (product) {
            setName(product.name ?? "");
            setPrice(product.price != null ? String(product.price) : "");
            setStock(product.stock != null ? String(product.stock) : "");
        } else {
            setName("");
            setPrice("");
            setStock("");
        }
        setError(null);
    }, [product, open]);

    if (!open) {
        return null;
    }

    async function handleSubmit(event: FormEvent) {
        event.preventDefault();
        setError(null);
        setSaving(true);

        const request: ProductRequest = {
            name: name.trim(),
            price: Number(price),
            stock: Number(stock),
        };

        try {
            let saved: ProductResponse;
            if (product != null) {
                saved = await ProductsService.updateProduct(product.id!, request);
            } else {
                saved = await ProductsService.createProduct(request);
            }
            onSaved(saved);
        } catch {
            setError("Failed to save product.");
        } finally {
            setSaving(false);
        }
    }

    return (
        <>
            <div className="modal fade show d-block" tabIndex={-1} role="dialog">
                <div className="modal-dialog" role="document">
                    <div className="modal-content">
                        <form onSubmit={handleSubmit}>
                            <div className="modal-header">
                                <h5 className="modal-title">
                                    {product ? "Edit product" : "Add product"}
                                </h5>
                                <button type="button" className="btn-close" aria-label="Close" onClick={onClose}/>
                            </div>
                            <div className="modal-body">
                                {error && (
                                    <div className="alert alert-danger mb-3">
                                        {error}
                                    </div>
                                )}
                                <div className="mb-3">
                                    <label className="form-label">Name</label>
                                    <input type="text" className="form-control" value={name} onChange={(event) => setName(event.target.value)} required/>
                                </div>
                                <div className="mb-3">
                                    <label className="form-label">Price</label>
                                    <input type="number" step="0.01" className="form-control" value={price} onChange={(event) => setPrice(event.target.value)}/>
                                </div>
                                <div className="mb-3">
                                    <label className="form-label">Stock</label>
                                    <input type="number" className="form-control" value={stock} onChange={(event) => setStock(event.target.value)}/>
                                </div>
                            </div>
                            <div className="modal-footer">
                                <button type="button" className="btn btn-secondary" onClick={onClose} disabled={saving}>Cancel</button>
                                <button type="submit" className="btn btn-primary" disabled={saving}>{saving ? "Saving..." : "Save"}</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <div className="modal-backdrop fade show" />
        </>
    );
}

export default ProductModal;
