type PaginationBarProps = {
    page: number;
    canGoPrevious: boolean;
    canGoNext: boolean;
    onPrevious: () => void;
    onNext: () => void;
};

function PaginationBar({ page, canGoPrevious, canGoNext, onPrevious, onNext }: PaginationBarProps) {
    return (
        <div className="d-flex justify-content-between align-items-center mt-3">
            <span>Page {page + 1}</span>
            <div>
                <button type="button" className="btn btn-outline-secondary me-2" onClick={onPrevious} disabled={!canGoPrevious}>Previous</button>
                <button type="button" className="btn btn-outline-secondary" onClick={onNext} disabled={!canGoNext}>Next</button>
            </div>
        </div>
    );
}

export default PaginationBar;
