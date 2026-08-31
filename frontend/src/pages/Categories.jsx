import { useEffect, useState } from "react";
import {
  getCategories,
  createCategory,
  updateCategory,
  deleteCategory,
} from "../services/categoryApi";

function Categories() {
  const [categories, setCategories] = useState([]);
  const [error, setError] = useState("");

  // 新增分類 Modal
  const [showModal, setShowModal] = useState(false);

  const [categoryName, setCategoryName] = useState("");
  const [categoryType, setCategoryType] = useState("EXPENSE");
  const [editingCategory, setEditingCategory] = useState(null);
  const [deletingCategory, setDeletingCategory] = useState(null);

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await getCategories();
        setCategories(response.data);
      } catch (error) {
        console.error("取得分類失敗:", error);
        setError("取得分類失敗");
      }
    };

    fetchCategories();
  }, []);

  const handleSaveCategory = async () => {
    const trimmedName = categoryName.trim();

    if (!trimmedName) {
      setError("分類名稱不可為空白");
      return;
    }

    try {
      const data = {
        name: trimmedName,
        type: categoryType,
      };

      if (editingCategory) {
        await updateCategory(editingCategory.id, data);
      } else {
        await createCategory(data);
      }

      const response = await getCategories();
      setCategories(response.data);

      setCategoryName("");
      setCategoryType("EXPENSE");
      setEditingCategory(null);
      setShowModal(false);
    } catch (error) {
      console.error("儲存分類失敗:", error);
      setError("儲存分類失敗");
    }
  };

  const handleDeleteCategory = async () => {
    if (!deletingCategory) {
      return;
    }

    try {
      await deleteCategory(deletingCategory.id);

      const response = await getCategories();
      setCategories(response.data);

      setDeletingCategory(null);
    } catch (error) {
      console.error("刪除分類失敗:", error);
      setError("刪除分類失敗");
    }
  };

  const incomeCategories = categories.filter(
    (category) => category.type === "INCOME",
  );

  const expenseCategories = categories.filter(
    (category) => category.type === "EXPENSE",
  );

  const renderCategoryTable = (categoryList, typeText) => {
    return (
      <div className="card">
        <div className="card-header">
          <h5 className="mb-0">{typeText}分類</h5>
        </div>

        <div className="card-body p-0">
          <div className="table-responsive">
            <table className="table table-striped table-hover align-middle mb-0 text-nowrap">
            <thead>
              <tr>
                <th>ID</th>
                <th>分類名稱</th>
                <th>類型</th>
                <th>操作</th>
              </tr>
            </thead>

            <tbody>
              {categoryList.map((category, index) => (
                <tr key={category.id}>
                  <td>{index + 1}</td>
                  <td>{category.name}</td>
                  <td>{typeText}</td>
                  <td>
                    <button
                      type="button"
                      className="btn btn-sm btn-outline-primary me-2"
                      disabled={category.defaultCategory}
                      onClick={() => {
                        setEditingCategory(category);
                        setCategoryName(category.name);
                        setCategoryType(category.type);
                        setShowModal(true);
                      }}
                    >
                      編輯
                    </button>

                    <button
                      type="button"
                      className="btn btn-sm btn-outline-danger"
                      disabled={category.defaultCategory}
                      onClick={() => setDeletingCategory(category)}
                    >
                      刪除
                    </button>
                  </td>
                </tr>
              ))}

              {categoryList.length === 0 && (
                <tr>
                  <td colSpan="4" className="text-center text-muted">
                    尚無分類
                  </td>
                </tr>
              )}
            </tbody>
            </table>
          </div>
        </div>
      </div>
    );
  };

  return (
    <div className="container-fluid px-4 py-4 page-container">
      {/* 標題 + 新增按鈕 */}
      <div className="page-header d-flex flex-wrap justify-content-between align-items-center gap-3">
        <h1 className="mb-0">分類管理</h1>

        <button
          type="button"
          className="btn btn-primary"
          onClick={() => setShowModal(true)}
        >
          ＋ 新增分類
        </button>
      </div>

      {error && <div className="alert alert-danger mt-3">{error}</div>}

      {/* 收入分類 */}
      <div className="mt-4">
        {renderCategoryTable(incomeCategories, "收入")}
      </div>

      {/* 支出分類 */}
      <div className="mt-4">
        {renderCategoryTable(expenseCategories, "支出")}
      </div>

      {/* 新增分類 Modal */}
      {showModal && (
        <div
          className="modal fade show d-block"
          tabIndex="-1"
          role="dialog"
          style={{ backgroundColor: "rgba(0, 0, 0, 0.5)" }}
        >
          <div className="modal-dialog" role="document">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">
                  {editingCategory ? "修改分類" : "新增分類"}
                </h5>

                <button
                  type="button"
                  className="btn-close"
                  onClick={() => {
                    setShowModal(false);
                    setEditingCategory(null);
                    setCategoryName("");
                    setCategoryType("EXPENSE");
                  }}
                ></button>
              </div>

              <div className="modal-body">
                <div className="mb-3">
                  <label htmlFor="categoryName" className="form-label">
                    分類名稱
                  </label>

                  <input
                    type="text"
                    className="form-control"
                    id="categoryName"
                    required
                    value={categoryName}
                    onChange={(e) => setCategoryName(e.target.value)}
                    placeholder="請輸入分類名稱"
                  />
                </div>

                <div className="mb-3">
                  <label htmlFor="categoryType" className="form-label">
                    類型
                  </label>

                  <select
                    className="form-select"
                    id="categoryType"
                    value={categoryType}
                    onChange={(e) => setCategoryType(e.target.value)}
                  >
                    <option value="INCOME">收入</option>
                    <option value="EXPENSE">支出</option>
                  </select>
                </div>
              </div>

              <div className="modal-footer">
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={() => {
                    setShowModal(false);
                    setEditingCategory(null);
                    setCategoryName("");
                    setCategoryType("EXPENSE");
                  }}
                >
                  取消
                </button>

                <button
                  type="button"
                  className="btn btn-primary"
                  onClick={handleSaveCategory}
                >
                  {editingCategory ? "儲存" : "新增"}
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
      {deletingCategory && (
        <div
          className="modal fade show d-block"
          tabIndex="-1"
          role="dialog"
          style={{ backgroundColor: "rgba(0, 0, 0, 0.5)" }}
        >
          <div className="modal-dialog" role="document">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">刪除分類</h5>

                <button
                  type="button"
                  className="btn-close"
                  onClick={() => setDeletingCategory(null)}
                ></button>
              </div>

              <div className="modal-body">
                <p>
                  確定要刪除分類「
                  <strong>{deletingCategory.name}</strong>
                  」嗎？
                </p>

                <p className="text-muted mb-0">
                  如果已有交易使用此分類，舊交易將會顯示為「未分類」。
                </p>
              </div>

              <div className="modal-footer">
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={() => setDeletingCategory(null)}
                >
                  取消
                </button>

                <button
                  type="button"
                  className="btn btn-danger"
                  onClick={handleDeleteCategory}
                >
                  刪除
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default Categories;
