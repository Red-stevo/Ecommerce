import "./Styles/ProductCategories.css";
import {Image} from "react-bootstrap";
import {GrFormEdit} from "react-icons/gr";
import {GoPlus} from "react-icons/go";
import {useState} from "react";
import AddIconForm from "./Components/AddIconForm.jsx";

const categories = [
    {categoryId:"GF5H65",categoryName:"Electronics",categoryIcon:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fcdn0.iconfinder.com%2Fdata%2Ficons%2Fabstract-electronics%2F64%2Fabstract_electronics-1024.png&f=1&nofb=1&ipt=cfefe1748bfabaa6ed6e7363980d9745511bd094e96a9764730bb92c9d24f101&ipo=images"},
    {categoryId:"GF5H66",categoryName:"cosmetics",categoryIcon:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.hUjsy5DqB1KkEzifTui7rgHaHa%26pid%3DApi&f=1&ipt=14cfa26ede8b62cc16846955310d8c6058fbc86c6d952a0ac9d675e3aea8cb23&ipo=images"},
    {categoryId:"GF5H67",categoryName:"Soaps",categoryIcon:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.vj7RGfg6J46iFqynBZZ9VgHaHa%26pid%3DApi&f=1&ipt=2970e2440b30206dea20f42f77e4274998f72340644f37f4471f0af1a5929bdb&ipo=images"},
]


const ProductCategories = () => {
    const [modalShow, setModalShow] = useState(false);

    return (
        <div className={"product-categories-page"}>
            <span className={"product-categories-header"}>Product Categories</span>

            <section className={"product-categories-body"}>
                <div className={"product-categories-body-header"}>
                    <span className={"product-categories-body-header-icon"}>Icon</span>
                    <span className={"product-categories-body-header-category"}>Category</span>
                    <span></span>
                </div>

                <div className={"product-categories-body-contents"} >
                    {categories.map(({categoryIcon, categoryId, categoryName}) => (
                        <div key={categoryId} className={"icon-content"}>
                            <Image className={"category-icon-image"} src={categoryIcon} />
                            <span className={"category-name-display"}>{categoryName}</span>
                            <GrFormEdit className={"edit-category-icon"} onClick={() => setModalShow(true)} />
                        </div>
                    ))}

                    <span className={"product-categories-add-category"} onClick={() => setModalShow(true)} >
                        <GoPlus className={"plus-category"} />
                    </span>
                </div>

            </section>

            <AddIconForm show={modalShow} onHide={() => setModalShow(false)}/>
        </div>
    );
};

export default ProductCategories;