import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import ProductUpload from "./upjoadpage..jsx";

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <ProductUpload/>
  </StrictMode>,
)
