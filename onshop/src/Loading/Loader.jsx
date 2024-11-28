import ReactLoading from 'react-loading';
import "./Loader.css";
const Loader = () => {
    return (
        <div className={"load-page"}>
            <ReactLoading
                type={"bars"}
                color={"#fcca03"}
                height={100}
                width={100} />
        </div>
    );
};

export default Loader;

