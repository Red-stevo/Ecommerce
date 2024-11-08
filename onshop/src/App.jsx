import './App.css'
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import MainHeader from "./IndexPage/Pages/IndexPage/MainHeader.jsx";
import IndexBody from "./IndexPage/Pages/IndexPage/IndexBody.jsx";

const App = () => {
  return (
      <Router>
          <Routes>
              <Route path={"/"} element={<MainHeader />}>
                 <Route path={"/"} element={<IndexBody />}/>
              </Route>
          </Routes>
      </Router>
  )
}

export default App
