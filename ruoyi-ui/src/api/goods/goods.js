import request  from "../../utils/request";

//删除
export function delGoods(id) {
  return request({
    url: '/goods/goods/delete'+id,
    method: 'delete'
  })
}
