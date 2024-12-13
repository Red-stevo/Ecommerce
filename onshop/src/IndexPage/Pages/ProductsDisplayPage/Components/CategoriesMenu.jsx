import {Image} from "react-bootstrap";
import {FcHome} from "react-icons/fc";
import {useLocation, useNavigate} from "react-router-dom";
import {useDispatch} from "react-redux";



const categories = [
    {categoryId:2, category:"Woman",iconUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse4.mm.bing.net%2Fth%3Fid%3DOIP.HYd1vkPEU3RaZY-QfVzbjwHaIP%26pid%3DApi&f=1&ipt=7e651592e4a55d0c2817d0c388d37648fc0dab09f32d1998c60530b310706925&ipo=images"},
    {categoryId:3, category:"Shoes", iconUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse4.mm.bing.net%2Fth%3Fid%3DOIP.EUKBm0WvK_8onUv-o__pSgHaHa%26pid%3DApi&f=1&ipt=9c0df4aacadaf5dc603440911bc27e0a01f8ef266aacdb7fb3847a385d2aa9da&ipo=images"},
    {categoryId:4, category:"Clothes",iconUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.zGB-Ar7FINJbSyX1BegTsgHaHa%26pid%3DApi&f=1&ipt=5373977a6690e0421fba13a005bf6b0e1237793dce528f62b7eef55b0313a7a9&ipo=images"},
    {categoryId:5, category:"Phones",iconUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.h8Oet2GAVxB4brsI-to_VQHaHa%26pid%3DApi&f=1&ipt=e79645f85bf6362353c25aa4f72584f66924cbfd7d324fcaa23272aeff2a9d07&ipo=images"},
    {categoryId:6, category:"Bags",iconUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse2.mm.bing.net%2Fth%3Fid%3DOIP.fvlHZIM5dBTOA5JfiYVe8AHaE7%26pid%3DApi&f=1&ipt=0258623ead84092a4b2f082521677de8a893d5855705890e323fcf7b5caae440&ipo=images"},
    {categoryId:1, category:"Men",iconUrl:"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.f3JjgY87yUMb3ULjzLLesgHaHa%26pid%3DApi&f=1&ipt=55d17f7c1b77413810431a223b1c18442d455aa6200e7c62dc92f1fdeb6e68a3&ipo=images"},
];


const CategoriesMenu = () => {
        const navigate= useNavigate();
        const dispatch = useDispatch();

        const handleCategoryFilter = (category) => {
            const  encodedQuery = encodeURIComponent(category.trim().replaceAll(' ', '+'));
            navigate(`/home/products/${encodedQuery}`);
        }


    return (
            <div className="product-category-menu hide-scrollbar" >
                <div className="categories" onClick={() => handleCategoryFilter("All Products")}>
                    <div className="category-name">Home</div>
                    <FcHome className="category-icon" />
                </div>
                { categories && categories.length > 0 &&
                    categories.map(({ category, categoryId, iconUrl }, index) => (

                    <div key={index} className="categories" onClick={() => handleCategoryFilter(category)}>
                        <div className="category-name">{category}</div>
                        <Image src={iconUrl} alt={`${category} icon`} height={50} width={50} className="category-icon" />
                    </div>
                ))}
            </div>
    );
};

export default CategoriesMenu;