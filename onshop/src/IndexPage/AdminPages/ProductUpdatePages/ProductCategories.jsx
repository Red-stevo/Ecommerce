import "./Styles/ProductCategories.css";

const ProductCategories = () => {
    return (
        <div className={"product-categories-page"}>
            <span className={"product-categories-header"}>Product Categories</span>

            <section className={"product-categories-body"}>
                <div className={"product-categories-body-header"}>
                    <span className={"product-categories-body-header-icon"}>Icon</span>
                    <span className={"product-categories-body-header-category"}>Category</span>
                </div>

            </section>
        </div>
    );
};

export default ProductCategories;