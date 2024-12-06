import  { useState } from "react";
import axios from "axios";

const ProductUpload = () => {
    const [productData, setProductData] = useState({
        productName: "",
        productDescription: "",
        productPrice: "",
        quantity: "",
        color: "",
        brand: "",
        aboutProduct: "",
        discount: "",
        buyingPrice: "",
        productUrls: [],
        categoryCreationRequestList: [
            { categoryName: "", categoryIcon: null }
        ]
    });

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setProductData({
            ...productData,
            [name]: value
        });
    };

    const handleFileChange = (e) => {
        const { name, files } = e.target;
        setProductData({
            ...productData,
            [name]: files
        });
    };

    const handleCategoryChange = (e, index) => {
        const { name, value } = e.target;
        const updatedCategories = [...productData.categoryCreationRequestList];
        updatedCategories[index] = {
            ...updatedCategories[index],
            [name]: value
        };
        setProductData({
            ...productData,
            categoryCreationRequestList: updatedCategories
        });
    };

    const handleCategoryFileChange = (e, index) => {
        const { name, files } = e.target;
        const updatedCategories = [...productData.categoryCreationRequestList];
        updatedCategories[index] = {
            ...updatedCategories[index],
            [name]: files[0]
        };
        setProductData({
            ...productData,
            categoryCreationRequestList: updatedCategories
        });
    };

    const addCategory = () => {
        setProductData({
            ...productData,
            categoryCreationRequestList: [
                ...productData.categoryCreationRequestList,
                { categoryName: "", categoryIcon: null }
            ]
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const formData = new FormData();
        for (const [key, value] of Object.entries(productData)) {
            if (Array.isArray(value)) {
                value.forEach((item, index) => {
                    if (item instanceof File) {
                        formData.append(`${key}[${index}]`, item);
                    } else if (typeof item === "string") {
                        formData.append(key, item);
                    } else {
                        Object.entries(item).forEach(([subKey, subValue]) => {
                            formData.append(`${key}[${index}].${subKey}`, subValue);
                        });
                    }
                });
            } else {
                formData.append(key, value);
            }
        }

        try {
            const response = await axios.post("http://localhost:8080/api/v1/products/post", formData, {
                headers: {
                    "Content-Type": "multipart/form-data",
                },
            });
            if (response.status === 200) {
                alert("Product uploaded successfully!");
            } else {
                alert(`Error uploading product: ${response.statusText}`);
            }
        } catch (error) {
            console.error("Error:", error);
            alert(`Error uploading product: ${error.message}`);
        }
    };

    return (
        <div>
            <h1>Upload Product</h1>
            <form onSubmit={handleSubmit} encType="multipart/form-data">
                {/* Text Inputs */}
                <label htmlFor="productName">Product Name:</label>
                <input
                    type="text"
                    id="productName"
                    name="productName"
                    value={productData.productName}
                    onChange={handleInputChange}
                    required
                />
                <br />
                <br />

                <label htmlFor="productDescription">Product Description:</label>
                <textarea
                    id="productDescription"
                    name="productDescription"
                    value={productData.productDescription}
                    onChange={handleInputChange}
                    required
                />
                <br />
                <br />

                <label htmlFor="productPrice">Product Price:</label>
                <input
                    type="number"
                    step="0.01"
                    id="productPrice"
                    name="productPrice"
                    value={productData.productPrice}
                    onChange={handleInputChange}
                    required
                />
                <br />
                <br />

                <label htmlFor="quantity">Quantity:</label>
                <input
                    type="number"
                    id="quantity"
                    name="quantity"
                    value={productData.quantity}
                    onChange={handleInputChange}
                    required
                />
                <br />
                <br />

                <label htmlFor="color">Color:</label>
                <input
                    type="text"
                    id="color"
                    name="color"
                    value={productData.color}
                    onChange={handleInputChange}
                    required
                />
                <br />
                <br />

                <label htmlFor="brand">Brand:</label>
                <input
                    type="text"
                    id="brand"
                    name="brand"
                    value={productData.brand}
                    onChange={handleInputChange}
                    required
                />
                <br />
                <br />

                <label htmlFor="aboutProduct">About Product:</label>
                <textarea
                    id="aboutProduct"
                    name="aboutProduct"
                    value={productData.aboutProduct}
                    onChange={handleInputChange}
                    required
                />
                <br />
                <br />

                <label htmlFor="discount">Discount (%):</label>
                <input
                    type="number"
                    step="0.01"
                    id="discount"
                    name="discount"
                    value={productData.discount}
                    onChange={handleInputChange}
                    required
                />
                <br />
                <br />

                <label htmlFor="buyingPrice">Buying Price:</label>
                <input
                    type="number"
                    step="0.01"
                    id="buyingPrice"
                    name="buyingPrice"
                    value={productData.buyingPrice}
                    onChange={handleInputChange}
                    required
                />
                <br />
                <br />

                {/* File Uploads */}
                <label htmlFor="productUrls">Product Images/Videos:</label>
                <input
                    type="file"
                    id="productUrls"
                    name="productUrls"
                    multiple
                    onChange={handleFileChange}
                />
                <br />
                <br />

                {/* Category Section */}
                <h3>Categories</h3>
                {productData.categoryCreationRequestList.map((category, index) => (
                    <div key={index}>
                        <label>Category Name:</label>
                        <input
                            type="text"
                            name={`categoryCreationRequestList[${index}].categoryName`}
                            value={category.categoryName}
                            onChange={(e) => handleCategoryChange(e, index)}
                        />
                        <br />
                        <br />
                        <label>Category Icon:</label>
                        <input
                            type="file"
                            name={`categoryCreationRequestList[${index}].categoryIcon`}
                            onChange={(e) => handleCategoryFileChange(e, index)}
                        />
                        <br />
                        <br />
                    </div>
                ))}
                <button type="button" onClick={addCategory}>
                    Add Another Category
                </button>
                <br />
                <br />

                {/* Submit Button */}
                <button type="submit">Submit Product</button>
            </form>
        </div>
    );
};

export default ProductUpload;
